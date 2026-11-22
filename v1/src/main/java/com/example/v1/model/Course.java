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
 * A class taught by one teacher.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	private String name;
	private String description;
	/** FK -> User (role TEACHER) */
	private Long teacherId;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
