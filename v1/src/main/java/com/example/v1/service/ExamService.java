package com.example.v1.service;

import com.example.v1.dao.ExamDao;
import com.example.v1.dao.ExamQuestionDao;
import com.example.v1.dao.QuestionDao;
import com.example.v1.dao.RubricCriterionDao;
import com.example.v1.dao.RubricDao;
import com.example.v1.dto.llm.GeneratedQuestion;
import com.example.v1.dto.llm.GeneratedQuestionSet;
import com.example.v1.dto.llm.QuestionGenerationInput;
import com.example.v1.dto.request.GenerateExamRequest;
import com.example.v1.dto.response.ExamResponse;
import com.example.v1.model.Exam;
import com.example.v1.model.ExamQuestion;
import com.example.v1.model.Question;
import com.example.v1.model.Rubric;
import com.example.v1.model.RubricCriterion;
import com.example.v1.model.SourceMaterial;
import com.example.v1.model.enums.ExamStatus;
import com.example.v1.model.enums.QuestionType;
import com.example.v1.service.llm.QuestionGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

	static final double ANSWER_POINTS = 1.0;
	static final double EXPLANATION_POINTS = 2.0;

	private final ExamDao examDao;
	private final ExamQuestionDao examQuestionDao;
	private final QuestionDao questionDao;
	private final RubricDao rubricDao;
	private final RubricCriterionDao rubricCriterionDao;
	private final MaterialService materialService;
	private final QuestionGenerator questionGenerator;

	/**
	 * Generates a test from the student's materials. Runs synchronously for now:
	 * the returned exam is READY, or FAILED with an errorMessage.
	 */
	public ExamResponse generate(GenerateExamRequest request) {
		List<SourceMaterial> materials = request.sourceMaterialIds().stream()
				.map(materialService::getEntity)
				.toList();
		for (SourceMaterial material : materials) {
			if (!Objects.equals(material.getUserId(), request.userId())) {
				throw new BadRequestException("Material " + material.getId() + " belongs to another user");
			}
		}

		Exam exam = examDao.save(Exam.builder()
				.userId(request.userId())
				.title(request.title() == null || request.title().isBlank()
						? materials.getFirst().getTitle() + " quiz"
						: request.title())
				.sourceMaterialIds(new ArrayList<>(request.sourceMaterialIds()))
				.difficulty(request.difficulty())
				.timeLimitMinutes(request.timeLimitMinutes())
				.build());

		String sourceText = materials.stream()
				.map(SourceMaterial::getContent)
				.collect(Collectors.joining("\n\n"));

		GeneratedQuestionSet generated;
		try {
			generated = questionGenerator.generate(new QuestionGenerationInput(
					sourceText,
					request.types() == null ? List.of() : request.types(),
					request.difficulty(),
					request.questionCount()));
		} catch (RuntimeException e) {
			log.error("Question generation failed for exam {}", exam.getId(), e);
			return fail(exam, "Question generation failed: " + e.getMessage());
		}

		int saved = 0;
		for (GeneratedQuestion g : generated.questions() == null ? List.<GeneratedQuestion>of() : generated.questions()) {
			List<String> options = defaultOptions(g.type(), g.options());
			if (!isUsable(g, options)) {
				// LLM output can be malformed: skip that question instead of failing the whole test
				log.warn("Skipping unusable generated question: {}", g);
				continue;
			}
			saveQuestion(exam, ++saved, g, options);
		}
		if (saved == 0) {
			return fail(exam, "No usable questions could be generated from this material");
		}

		exam.setStatus(ExamStatus.READY);
		examDao.save(exam);
		return ExamResponse.from(exam, saved);
	}

	public List<ExamResponse> findByUser(Long userId) {
		return examDao.findByUserId(userId).stream().map(this::toResponse).toList();
	}

	public ExamResponse get(Long id) {
		return toResponse(getEntity(id));
	}

	Exam getEntity(Long id) {
		return examDao.findById(id).orElseThrow(() -> new NotFoundException("Exam", id));
	}

	private ExamResponse toResponse(Exam exam) {
		return ExamResponse.from(exam, examQuestionDao.findByExamId(exam.getId()).size());
	}

	private ExamResponse fail(Exam exam, String reason) {
		exam.setStatus(ExamStatus.FAILED);
		exam.setErrorMessage(reason);
		examDao.save(exam);
		return ExamResponse.from(exam, 0);
	}

	private void saveQuestion(Exam exam, int position, GeneratedQuestion g, List<String> options) {
		Question question = questionDao.save(Question.builder()
				.type(g.type())
				.stem(g.stem())
				.options(new ArrayList<>(options))
				.correctAnswer(g.correctAnswer())
				.explanationPrompt(g.explanationPrompt() == null || g.explanationPrompt().isBlank()
						? "Explain your answer."
						: g.explanationPrompt())
				.difficulty(g.difficulty())
				.bloomLevel(g.bloomLevel())
				.build());

		Rubric rubric = rubricDao.save(Rubric.builder()
				.questionId(question.getId())
				.referenceExplanation(g.referenceExplanation())
				.commonMisconceptions(g.commonMisconceptions() == null
						? new ArrayList<>() : new ArrayList<>(g.commonMisconceptions()))
				.build());
		g.criteria().forEach(c -> rubricCriterionDao.save(RubricCriterion.builder()
				.rubricId(rubric.getId())
				.keyConcept(c.keyConcept())
				.description(c.description())
				.points(c.points())
				.required(c.required())
				.build()));

		examQuestionDao.save(ExamQuestion.builder()
				.examId(exam.getId())
				.questionId(question.getId())
				.position(position)
				.answerPoints(ANSWER_POINTS)
				.explanationPoints(EXPLANATION_POINTS)
				.build());
	}

	private static List<String> defaultOptions(QuestionType type, List<String> options) {
		if (type == QuestionType.TRUE_FALSE && (options == null || options.isEmpty())) {
			return List.of("True", "False");
		}
		return options == null ? List.of() : options;
	}

	/** Rejects generated questions that can't be graded properly. */
	private static boolean isUsable(GeneratedQuestion g, List<String> options) {
		if (g.type() == null || isBlank(g.stem()) || isBlank(g.correctAnswer())
				|| g.criteria() == null || g.criteria().isEmpty()
				|| g.criteria().stream().anyMatch(c -> isBlank(c.keyConcept()) || c.points() <= 0)) {
			return false;
		}
		String correct = AnswerChecker.normalize(g.correctAnswer());
		return switch (g.type()) {
			case MULTIPLE_CHOICE -> options.size() >= 2
					&& options.stream().anyMatch(o -> AnswerChecker.normalize(o).equals(correct));
			case TRUE_FALSE -> correct.equals("true") || correct.equals("false");
			case NUMERIC -> isNumber(g.correctAnswer());
			case SHORT_ANSWER, OPEN -> true;
		};
	}

	private static boolean isNumber(String s) {
		try {
			Double.parseDouble(s.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static boolean isBlank(String s) {
		return s == null || s.isBlank();
	}
}
