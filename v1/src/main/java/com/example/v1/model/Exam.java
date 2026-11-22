package com.example.v1.model;

import com.example.v1.model.enums.Difficulty;
import com.example.v1.model.enums.ExamStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A test generated for a student from their materials. Named Exam to avoid clashing with JUnit's @Test.
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

	/** FK -> User (owner) */
	private Long userId;
	private String title;
	/** Materials the questions were generated from */
	@Builder.Default
	private List<Long> sourceMaterialIds = new ArrayList<>();
	private Difficulty difficulty;
	/** null = no limit */
	private Integer timeLimitMinutes;
	@Builder.Default
	private boolean shuffleQuestions = false;
	@Builder.Default
	private int minExplanationWords = 15;
	/** How many probing follow-up questions the LLM may ask per answer */
	@Builder.Default
	private int maxFollowUps = 1;
	@Builder.Default
	private ExamStatus status = ExamStatus.GENERATING;
	/** Why generation FAILED, if it did */
	private String errorMessage;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
