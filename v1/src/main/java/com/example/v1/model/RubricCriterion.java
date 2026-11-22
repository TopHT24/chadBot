package com.example.v1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * One gradable point of a rubric, e.g. "mentions chlorophyll absorbs light".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RubricCriterion implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> Rubric */
	private Long rubricId;
	private String keyConcept;
	private String description;
	private double points;
	/** If true, missing it caps the explanation score */
	private boolean required;
}
