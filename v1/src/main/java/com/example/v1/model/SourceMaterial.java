package com.example.v1.model;

import com.example.v1.model.enums.SourceType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Course material that questions are generated from.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SourceMaterial implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> Course */
	private Long courseId;
	private String title;
	private SourceType type;
	/** Extracted plain text sent to the LLM */
	private String content;
	/** Path/URL of the original file, if any */
	private String fileRef;
	/** FK -> User */
	private Long uploadedBy;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
