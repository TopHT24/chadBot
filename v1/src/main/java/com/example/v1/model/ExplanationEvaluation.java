package com.example.v1.model;

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
 * The LLM's grading of an explanation against the rubric. Re-grading adds a new row instead of overwriting.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExplanationEvaluation implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> StudentAnswer */
	private Long studentAnswerId;
	/** JSONB in Postgres later */
	@Builder.Default
	private List<CriterionResult> criteriaResults = new ArrayList<>();
	@Builder.Default
	private List<String> misconceptionsDetected = new ArrayList<>();
	/** 0..1, computed in code from criteriaResults */
	private double explanationScore;
	/** Does the reasoning actually support the chosen answer? */
	private Boolean consistentWithAnswer;
	private String feedbackToStudent;
	/** 0..1, grader's self-reported confidence */
	private double confidence;
	/** FK -> LlmCall */
	private Long llmCallId;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
