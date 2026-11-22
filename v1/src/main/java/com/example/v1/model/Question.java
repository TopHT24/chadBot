package com.example.v1.model;

import com.example.v1.model.enums.BloomLevel;
import com.example.v1.model.enums.Difficulty;
import com.example.v1.model.enums.QuestionType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A question generated from the student's material. Every question requires an explanation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Question implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	private QuestionType type;
	/** The question text */
	private String stem;
	/** Choices for MULTIPLE_CHOICE / TRUE_FALSE; empty otherwise */
	@Builder.Default
	private List<String> options = new ArrayList<>();
	/** Never shown to the student before grading */
	private String correctAnswer;
	/** e.g. "Explain why you chose this answer" */
	private String explanationPrompt;
	private Difficulty difficulty;
	private BloomLevel bloomLevel;
	/** FK -> LlmCall that generated it */
	private Long llmCallId;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
