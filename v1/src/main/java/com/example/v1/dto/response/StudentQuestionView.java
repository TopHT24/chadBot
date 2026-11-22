package com.example.v1.dto.response;

import com.example.v1.model.ExamQuestion;
import com.example.v1.model.Question;
import com.example.v1.model.enums.QuestionType;

import java.util.List;

/**
 * What the student sees while answering: no correct answer, no rubric.
 * Every question requires an explanation.
 */
public record StudentQuestionView(Long questionId, int position, QuestionType type, String stem,
		List<String> options, String explanationPrompt, double totalPoints) {

	public static StudentQuestionView from(ExamQuestion eq, Question q) {
		return new StudentQuestionView(q.getId(), eq.getPosition(), q.getType(), q.getStem(),
				List.copyOf(q.getOptions()), q.getExplanationPrompt(),
				eq.getAnswerPoints() + eq.getExplanationPoints());
	}
}
