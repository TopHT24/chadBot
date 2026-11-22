package com.example.v1.model;

import com.example.v1.model.enums.AttemptStatus;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * One sitting of an exam. A student can retake an exam, giving several attempts.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Attempt implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> Exam */
	private Long examId;
	/** FK -> User */
	private Long userId;
	private int attemptNumber;
	@Builder.Default
	private AttemptStatus status = AttemptStatus.IN_PROGRESS;
	@Builder.Default
	private Instant startedAt = Instant.now();
	private Instant submittedAt;
	private Double answerScore;
	private Double explanationScore;
	private Double totalScore;
}
