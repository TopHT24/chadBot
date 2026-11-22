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
 * What a good explanation for a question must contain. One per question.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rubric implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> Question (unique) */
	private Long questionId;
	/** Model answer given to the grader */
	private String referenceExplanation;
	/** Wrong ideas the grader should look for */
	@Builder.Default
	private List<String> commonMisconceptions = new ArrayList<>();
	@Builder.Default
	private Instant createdAt = Instant.now();
}
