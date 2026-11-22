package com.example.v1.model;

import com.example.v1.model.enums.PromptPurpose;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Audit log of every LLM request: debugging, cost tracking, re-grading.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LlmCall implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> PromptTemplate */
	private Long promptTemplateId;
	private PromptPurpose purpose;
	private String model;
	private String requestPayload;
	private String rawResponse;
	/** Did the response match the expected JSON schema? */
	private boolean parsedOk;
	private String errorMessage;
	private Integer inputTokens;
	private Integer outputTokens;
	private BigDecimal costUsd;
	private Long latencyMs;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
