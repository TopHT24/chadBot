package com.example.v1.dto.llm;

import com.example.v1.model.enums.BloomLevel;
import com.example.v1.model.enums.Difficulty;
import com.example.v1.model.enums.QuestionType;

import java.util.List;

/**
 * One question proposed by the LLM, with the rubric for its explanation.
 * Becomes a DRAFT Question + Rubric + RubricCriterion rows.
 */
public record GeneratedQuestion(
		QuestionType type,
		String stem,
		List<String> options,
		String correctAnswer,
		String explanationPrompt,
		Difficulty difficulty,
		BloomLevel bloomLevel,
		/* id of one of the objectives passed in the prompt, or null */
		Long objectiveId,
		String referenceExplanation,
		List<GeneratedCriterion> criteria,
		List<String> commonMisconceptions) {
}
