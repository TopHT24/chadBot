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
 * Links a student to a course.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Enrollment implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> Course */
	private Long courseId;
	/** FK -> User (role STUDENT) */
	private Long studentId;
	@Builder.Default
	private Instant enrolledAt = Instant.now();
}
