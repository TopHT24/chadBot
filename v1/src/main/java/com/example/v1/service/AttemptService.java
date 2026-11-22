package com.example.v1.service;

import com.example.v1.dao.AttemptDao;
import com.example.v1.dao.ExamQuestionDao;
import com.example.v1.dao.QuestionDao;
import com.example.v1.dao.StudentAnswerDao;
import com.example.v1.dto.request.SubmitAnswerRequest;
import com.example.v1.dto.response.AttemptResponse;
import com.example.v1.dto.response.AttemptResultResponse;
import com.example.v1.dto.response.StudentAnswerResponse;
import com.example.v1.dto.response.StudentExamView;
import com.example.v1.dto.response.StudentQuestionView;
import com.example.v1.model.Attempt;
import com.example.v1.model.Exam;
import com.example.v1.model.ExamQuestion;
import com.example.v1.model.Question;
import com.example.v1.model.StudentAnswer;
import com.example.v1.model.enums.AttemptStatus;
import com.example.v1.model.enums.ExamStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AttemptService {

	private final AttemptDao attemptDao;
	private final ExamQuestionDao examQuestionDao;
	private final QuestionDao questionDao;
	private final StudentAnswerDao studentAnswerDao;
	private final ExamService examService;
	private final GradingService gradingService;

	/** Starts an attempt, or returns the one already in progress. Retakes are unlimited. */
	public StudentExamView start(Long examId, Long userId) {
		Exam exam = examService.getEntity(examId);
		if (!Objects.equals(exam.getUserId(), userId)) {
			throw new BadRequestException("Exam " + examId + " belongs to another user");
		}
		if (exam.getStatus() != ExamStatus.READY) {
			throw new BadRequestException("Exam " + examId + " is not ready (status " + exam.getStatus() + ")");
		}

		List<Attempt> previous = attemptDao.findByExamIdAndUserId(examId, userId);
		Optional<Attempt> inProgress = previous.stream()
				.filter(a -> a.getStatus() == AttemptStatus.IN_PROGRESS)
				.findFirst();
		Attempt attempt = inProgress.orElseGet(() -> attemptDao.save(Attempt.builder()
				.examId(examId)
				.userId(userId)
				.attemptNumber(previous.size() + 1)
				.build()));
		return view(attempt, exam);
	}

	public List<AttemptResponse> findByUser(Long userId) {
		return attemptDao.findByUserId(userId).stream().map(AttemptResponse::from).toList();
	}

	public StudentExamView getView(Long attemptId) {
		Attempt attempt = getEntity(attemptId);
		return view(attempt, examService.getEntity(attempt.getExamId()));
	}

	/** Saves the answer and explanation for one question. Can be called again to revise them. */
	public StudentAnswerResponse answer(Long attemptId, SubmitAnswerRequest request) {
		Attempt attempt = getEntity(attemptId);
		requireInProgress(attempt);
		Exam exam = examService.getEntity(attempt.getExamId());
		requireWithinTimeLimit(attempt, exam);

		examQuestionDao.findByExamIdAndQuestionId(exam.getId(), request.questionId())
				.orElseThrow(() -> new BadRequestException("Question " + request.questionId() + " is not part of this exam"));
		int words = countWords(request.explanation());
		if (words < exam.getMinExplanationWords()) {
			throw new BadRequestException("Your explanation must be at least " + exam.getMinExplanationWords()
					+ " words (it has " + words + ")");
		}

		StudentAnswer answer = studentAnswerDao.findByAttemptIdAndQuestionId(attemptId, request.questionId())
				.map(existing -> {
					existing.setRevisionCount(existing.getRevisionCount() + 1);
					return existing;
				})
				.orElseGet(() -> StudentAnswer.builder().attemptId(attemptId).questionId(request.questionId()).build());
		answer.setAnswer(request.answer());
		answer.setExplanation(request.explanation());
		if (request.timeSpentSeconds() != null) {
			int before = answer.getTimeSpentSeconds() == null ? 0 : answer.getTimeSpentSeconds();
			answer.setTimeSpentSeconds(before + request.timeSpentSeconds());
		}
		answer.setSubmittedAt(Instant.now());
		return StudentAnswerResponse.from(studentAnswerDao.save(answer));
	}

	/** Ends the attempt and grades it. Allowed even after the time limit has passed. */
	public AttemptResultResponse submit(Long attemptId) {
		Attempt attempt = getEntity(attemptId);
		requireInProgress(attempt);
		return gradingService.grade(attempt, examService.getEntity(attempt.getExamId()));
	}

	public AttemptResultResponse result(Long attemptId) {
		Attempt attempt = getEntity(attemptId);
		if (attempt.getStatus() != AttemptStatus.GRADED) {
			throw new BadRequestException("Attempt " + attemptId + " has not been graded yet");
		}
		return gradingService.buildResult(attempt);
	}

	private StudentExamView view(Attempt attempt, Exam exam) {
		List<ExamQuestion> examQuestions = new ArrayList<>(examQuestionDao.findByExamId(exam.getId()));
		if (exam.isShuffleQuestions()) {
			// seeded with the attempt id, so the order stays the same when the student reloads
			Collections.shuffle(examQuestions, new Random(attempt.getId()));
		}
		List<StudentQuestionView> questions = examQuestions.stream()
				.map(eq -> StudentQuestionView.from(eq, getQuestion(eq.getQuestionId())))
				.toList();
		return StudentExamView.from(exam, attempt, questions);
	}

	private Question getQuestion(Long id) {
		return questionDao.findById(id).orElseThrow(() -> new NotFoundException("Question", id));
	}

	private Attempt getEntity(Long id) {
		return attemptDao.findById(id).orElseThrow(() -> new NotFoundException("Attempt", id));
	}

	private static void requireInProgress(Attempt attempt) {
		if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
			throw new BadRequestException("Attempt " + attempt.getId() + " was already submitted");
		}
	}

	private static void requireWithinTimeLimit(Attempt attempt, Exam exam) {
		if (exam.getTimeLimitMinutes() == null) {
			return;
		}
		Instant deadline = attempt.getStartedAt().plus(Duration.ofMinutes(exam.getTimeLimitMinutes()));
		if (Instant.now().isAfter(deadline)) {
			throw new BadRequestException("Time is up: submit the attempt");
		}
	}

	private static int countWords(String text) {
		return text == null || text.isBlank() ? 0 : text.trim().split("\\s+").length;
	}
}
