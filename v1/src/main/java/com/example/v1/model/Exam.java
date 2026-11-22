package com.example.v1.model;

import com.example.v1.model.enums.ExamStatus;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A test students take. Named Exam to avoid clashing with JUnit's @Test.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Exam implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> Course */
	private Long courseId;
	private String title;
	private String instructions;
	/** FK -> User */
	private Long createdBy;
	/** null = no limit */
	private Integer timeLimitMinutes;
	@Builder.Default
	private int maxAttempts = 1;
	@Builder.Default
	private boolean shuffleQuestions = false;
	@Builder.Default
	private int minExplanationWords = 15;
	/** How many probing follow-up questions the LLM may ask per answer */
	@Builder.Default
	private int maxFollowUps = 1;
	@Builder.Default
	private ExamStatus status = ExamStatus.DRAFT;
	private Instant opensAt;
	private Instant closesAt;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
