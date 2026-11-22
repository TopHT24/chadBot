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
 * Something students should be able to do; questions and mastery reports hang off it.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LearningObjective implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> Course */
	private Long courseId;
	private String description;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
