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
 * A student using the app.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	private String fullName;
	private String email;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
