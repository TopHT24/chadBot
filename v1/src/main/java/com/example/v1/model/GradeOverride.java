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
 * A teacher's correction of an LLM grade. Keeps the history.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GradeOverride implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> ExplanationEvaluation */
	private Long evaluationId;
	/** FK -> User */
	private Long teacherId;
	private double previousScore;
	private double newScore;
	private String reason;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
