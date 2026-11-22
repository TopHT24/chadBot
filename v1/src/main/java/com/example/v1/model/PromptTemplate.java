package com.example.v1.model;

import com.example.v1.model.enums.PromptPurpose;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A versioned prompt, so grades can be traced to and re-run with a specific prompt.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PromptTemplate implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** e.g. "grade_explanation" */
	private String name;
	private int version;
	private PromptPurpose purpose;
	private String systemPrompt;
	/** With {{placeholders}} */
	private String userTemplate;
	/** Model id this prompt was tuned for */
	private String model;
	/** Only one active version per name */
	@Builder.Default
	private boolean active = false;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
