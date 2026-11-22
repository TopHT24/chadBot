package com.example.v1.model;

import com.example.v1.model.enums.BloomLevel;
import com.example.v1.model.enums.Difficulty;
import com.example.v1.model.enums.QuestionOrigin;
import com.example.v1.model.enums.QuestionStatus;
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
 * A reusable question. Generated questions start as DRAFT and need teacher approval.
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

	/** FK -> Course */
	private Long courseId;
	/** FK -> LearningObjective (nullable) */
	private Long objectiveId;
	private QuestionType type;
	/** The question text */
	private String stem;
	/** Choices for MULTIPLE_CHOICE / TRUE_FALSE; empty otherwise */
	@Builder.Default
	private List<String> options = new ArrayList<>();
	/** Never expose to students before grading */
	private String correctAnswer;
	@Builder.Default
	private boolean requiresExplanation = true;
	/** e.g. "Explain why you chose this answer" */
	private String explanationPrompt;
	private Difficulty difficulty;
	private BloomLevel bloomLevel;
	private QuestionOrigin origin;
	@Builder.Default
	private QuestionStatus status = QuestionStatus.DRAFT;
	/** Teacher's note when approving/rejecting */
	private String reviewNote;
	/** FK -> LlmCall that generated it (null if teacher-written) */
	private Long llmCallId;
	/** FK -> User */
	private Long createdBy;
	@Builder.Default
	private Instant createdAt = Instant.now();
	@Builder.Default
	private Instant updatedAt = Instant.now();
}
