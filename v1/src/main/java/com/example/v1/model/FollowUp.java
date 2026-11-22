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
 * A probing question the LLM asks when an explanation is vague, and the student's reply.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FollowUp implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> StudentAnswer */
	private Long studentAnswerId;
	/** 1, 2, ... up to Exam.maxFollowUps */
	private int round;
	private String question;
	private String studentReply;
	/** FK -> LlmCall */
	private Long llmCallId;
	@Builder.Default
	private Instant askedAt = Instant.now();
	private Instant repliedAt;
}
