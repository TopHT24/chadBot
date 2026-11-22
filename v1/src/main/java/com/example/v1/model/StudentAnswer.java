package com.example.v1.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A student's answer AND explanation to one question in an attempt.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StudentAnswer implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> Attempt */
	private Long attemptId;
	/** FK -> Question */
	private Long questionId;
	/** Chosen option / value / text */
	private String answer;
	/** The student's reasoning */
	private String explanation;
	/** Computed in code, not by the LLM, where the type allows */
	private Boolean answerCorrect;
	private Double answerScore;
	private Integer timeSpentSeconds;
	/** Signal for pasted / outsourced explanations */
	@Builder.Default
	private int revisionCount = 0;
	private Instant submittedAt;
}
