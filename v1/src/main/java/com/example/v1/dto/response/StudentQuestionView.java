package com.example.v1.dto.response;

import com.example.v1.model.ExamQuestion;
import com.example.v1.model.Question;
import com.example.v1.model.enums.QuestionType;

import java.util.List;

/**
 * What a student sees while taking an exam: no correct answer, no rubric.
 */
public record StudentQuestionView(Long questionId, int position, QuestionType type, String stem,
		List<String> options, boolean requiresExplanation, String explanationPrompt, double totalPoints) {

	public static StudentQuestionView from(ExamQuestion eq, Question q) {
		return new StudentQuestionView(q.getId(), eq.getPosition(), q.getType(), q.getStem(),
				List.copyOf(q.getOptions()), q.isRequiresExplanation(), q.getExplanationPrompt(),
				eq.getAnswerPoints() + eq.getExplanationPoints());
	}
}
