package com.example.v1.service;

import com.example.v1.dao.AttemptDao;
import com.example.v1.dao.ExamQuestionDao;
import com.example.v1.dao.ExplanationEvaluationDao;
import com.example.v1.dao.FollowUpDao;
import com.example.v1.dao.QuestionDao;
import com.example.v1.dao.RubricCriterionDao;
import com.example.v1.dao.RubricDao;
import com.example.v1.dao.StudentAnswerDao;
import com.example.v1.dto.llm.ExplanationGradingInput;
import com.example.v1.dto.llm.ExplanationGradingResult;
import com.example.v1.dto.llm.GradedCriterion;
import com.example.v1.dto.response.AttemptResponse;
import com.example.v1.dto.response.AttemptResultResponse;
import com.example.v1.dto.response.AttemptResultResponse.QuestionResult;
import com.example.v1.dto.response.FollowUpResponse;
import com.example.v1.model.Attempt;
import com.example.v1.model.CriterionResult;
import com.example.v1.model.Exam;
import com.example.v1.model.ExamQuestion;
import com.example.v1.model.ExplanationEvaluation;
import com.example.v1.model.FollowUp;
import com.example.v1.model.Question;
import com.example.v1.model.Rubric;
import com.example.v1.model.RubricCriterion;
import com.example.v1.model.StudentAnswer;
import com.example.v1.model.enums.AttemptStatus;
import com.example.v1.service.llm.ExplanationGrader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Grades a submitted attempt: answers in code (AnswerChecker), explanations with
 * the ExplanationGrader (the LLM). The LLM says which rubric criteria are met;
 * the points are computed here.
 */
@Service
@RequiredArgsConstructor
public class GradingService {

	/** Explanation score cap when a required rubric criterion is missing. */
	static final double REQUIRED_CRITERION_MISSING_CAP = 0.5;

	private final AttemptDao attemptDao;
	private final ExamQuestionDao examQuestionDao;
	private final QuestionDao questionDao;
	private final StudentAnswerDao studentAnswerDao;
	private final RubricDao rubricDao;
	private final RubricCriterionDao rubricCriterionDao;
	private final ExplanationEvaluationDao evaluationDao;
	private final FollowUpDao followUpDao;
	private final AnswerChecker answerChecker;
	private final ExplanationGrader explanationGrader;

	public AttemptResultResponse grade(Attempt attempt, Exam exam) {
		attempt.setStatus(AttemptStatus.GRADING);
		attempt.setSubmittedAt(Instant.now());
		attemptDao.save(attempt);

		for (ExamQuestion eq : examQuestionDao.findByExamId(attempt.getExamId())) {
			Optional<StudentAnswer> maybeAnswer =
					studentAnswerDao.findByAttemptIdAndQuestionId(attempt.getId(), eq.getQuestionId());
			if (maybeAnswer.isEmpty()) {
				continue; // unanswered: scores 0
			}
			StudentAnswer answer = maybeAnswer.get();
			Question question = getQuestion(eq.getQuestionId());

			Boolean correct = answerChecker.check(question, answer.getAnswer());
			answer.setAnswerCorrect(correct);
			answer.setAnswerScore(Boolean.TRUE.equals(correct) ? eq.getAnswerPoints() : 0.0);
			studentAnswerDao.save(answer);

			rubricDao.findByQuestionId(question.getId())
					.ifPresent(rubric -> evaluateExplanation(question, answer, rubric, exam.getMaxFollowUps()));
		}

		List<QuestionResult> results = questionResults(attempt);
		double answerScore = results.stream().mapToDouble(QuestionResult::answerPointsEarned).sum();
		double explanationScore = results.stream().mapToDouble(QuestionResult::explanationPointsEarned).sum();
		attempt.setAnswerScore(round(answerScore));
		attempt.setExplanationScore(round(explanationScore));
		attempt.setTotalScore(round(answerScore + explanationScore));
		attempt.setStatus(AttemptStatus.GRADED);
		attemptDao.save(attempt);
		return new AttemptResultResponse(AttemptResponse.from(attempt), results);
	}

	public AttemptResultResponse buildResult(Attempt attempt) {
		return new AttemptResultResponse(AttemptResponse.from(attempt), questionResults(attempt));
	}

	private void evaluateExplanation(Question question, StudentAnswer answer, Rubric rubric, int maxFollowUps) {
		List<RubricCriterion> criteria = rubricCriterionDao.findByRubricId(rubric.getId());
		List<FollowUp> followUps = followUpDao.findByStudentAnswerId(answer.getId());
		ExplanationGradingInput input = new ExplanationGradingInput(
				question.getStem(),
				question.getOptions(),
				question.getCorrectAnswer(),
				answer.getAnswer(),
				Boolean.TRUE.equals(answer.getAnswerCorrect()),
				answer.getExplanation(),
				rubric.getReferenceExplanation(),
				criteria.stream()
						.map(c -> new ExplanationGradingInput.Criterion(c.getId(), c.getKeyConcept(), c.getDescription(), c.isRequired()))
						.toList(),
				rubric.getCommonMisconceptions(),
				followUps.stream()
						.map(f -> new ExplanationGradingInput.PriorFollowUp(f.getQuestion(), f.getStudentReply()))
						.toList());

		ExplanationGradingResult result = explanationGrader.grade(input);

		Map<Long, GradedCriterion> gradedById = result.criteria() == null ? Map.of()
				: result.criteria().stream()
						.filter(g -> g.criterionId() != null)
						.collect(Collectors.toMap(GradedCriterion::criterionId, Function.identity(), (a, b) -> a));
		// criteria the grader skipped or invented are ignored: only the rubric's own criteria count
		List<CriterionResult> criteriaResults = criteria.stream()
				.map(c -> {
					GradedCriterion g = gradedById.get(c.getId());
					boolean met = g != null && g.met();
					return new CriterionResult(c.getId(), met, met ? g.evidenceQuote() : null, met ? c.getPoints() : 0.0);
				})
				.toList();

		evaluationDao.save(ExplanationEvaluation.builder()
				.studentAnswerId(answer.getId())
				.criteriaResults(new ArrayList<>(criteriaResults))
				.misconceptionsDetected(result.misconceptionsDetected() == null
						? new ArrayList<>() : new ArrayList<>(result.misconceptionsDetected()))
				.explanationScore(explanationScore(criteria, criteriaResults))
				.consistentWithAnswer(result.consistentWithAnswer())
				.feedbackToStudent(result.feedbackToStudent())
				.confidence(Math.clamp(result.confidence(), 0.0, 1.0))
				.build());

		// the grader found the explanation vague: ask the student to go further
		if (result.followUpQuestion() != null && !result.followUpQuestion().isBlank()
				&& followUps.size() < maxFollowUps) {
			followUpDao.save(FollowUp.builder()
					.studentAnswerId(answer.getId())
					.round(followUps.size() + 1)
					.question(result.followUpQuestion())
					.build());
		}
	}

	/** Share of rubric points earned (0..1), capped when a required criterion is missing. */
	static double explanationScore(List<RubricCriterion> criteria, List<CriterionResult> results) {
		double max = criteria.stream().mapToDouble(RubricCriterion::getPoints).sum();
		if (max <= 0) {
			return 0.0;
		}
		double score = results.stream().mapToDouble(CriterionResult::pointsAwarded).sum() / max;
		boolean requiredMissing = criteria.stream()
				.filter(RubricCriterion::isRequired)
				.anyMatch(c -> results.stream().noneMatch(r -> c.getId().equals(r.criterionId()) && r.met()));
		return requiredMissing ? Math.min(score, REQUIRED_CRITERION_MISSING_CAP) : score;
	}

	private List<QuestionResult> questionResults(Attempt attempt) {
		List<QuestionResult> rows = new ArrayList<>();
		for (ExamQuestion eq : examQuestionDao.findByExamId(attempt.getExamId())) {
			Question question = getQuestion(eq.getQuestionId());
			double maxPoints = eq.getAnswerPoints() + eq.getExplanationPoints();
			Optional<Rubric> rubric = rubricDao.findByQuestionId(question.getId());
			String modelExplanation = rubric.map(Rubric::getReferenceExplanation).orElse(null);

			Optional<StudentAnswer> maybeAnswer =
					studentAnswerDao.findByAttemptIdAndQuestionId(attempt.getId(), eq.getQuestionId());
			if (maybeAnswer.isEmpty()) {
				rows.add(new QuestionResult(question.getId(), question.getStem(), null, null, false,
						question.getCorrectAnswer(), modelExplanation, 0, 0, maxPoints, false,
						List.of(), List.of(), List.of(), "Not answered.", List.of()));
				continue;
			}
			StudentAnswer answer = maybeAnswer.get();
			Optional<ExplanationEvaluation> evaluation = evaluationDao.findLatestByStudentAnswerId(answer.getId());

			Map<Long, String> conceptById = rubric
					.map(r -> rubricCriterionDao.findByRubricId(r.getId()))
					.orElse(List.of())
					.stream()
					.collect(Collectors.toMap(RubricCriterion::getId, RubricCriterion::getKeyConcept));
			List<CriterionResult> criteriaResults = evaluation.map(ExplanationEvaluation::getCriteriaResults).orElse(List.of());

			rows.add(new QuestionResult(
					question.getId(),
					question.getStem(),
					answer.getAnswer(),
					answer.getExplanation(),
					answer.getAnswerCorrect(),
					question.getCorrectAnswer(),
					modelExplanation,
					answer.getAnswerScore() == null ? 0 : answer.getAnswerScore(),
					round(evaluation.map(e -> e.getExplanationScore() * eq.getExplanationPoints()).orElse(0.0)),
					maxPoints,
					Boolean.TRUE.equals(answer.getAnswerCorrect())
							&& evaluation.map(e -> Boolean.FALSE.equals(e.getConsistentWithAnswer())).orElse(false),
					criteriaResults.stream().filter(CriterionResult::met).map(r -> conceptById.get(r.criterionId())).toList(),
					criteriaResults.stream().filter(r -> !r.met()).map(r -> conceptById.get(r.criterionId())).toList(),
					evaluation.map(ExplanationEvaluation::getMisconceptionsDetected).orElse(List.of()),
					evaluation.map(ExplanationEvaluation::getFeedbackToStudent).orElse(null),
					followUpDao.findByStudentAnswerId(answer.getId()).stream().map(FollowUpResponse::from).toList()));
		}
		return rows;
	}

	private Question getQuestion(Long id) {
		return questionDao.findById(id).orElseThrow(() -> new NotFoundException("Question", id));
	}

	private static double round(double value) {
		return Math.round(value * 100.0) / 100.0;
	}
}
