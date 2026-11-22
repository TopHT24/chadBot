package com.example.v1.model;

import com.example.v1.model.enums.Role;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A person using the app: student, teacher or admin.
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
	private Role role;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
