package com.example.v1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Puts a question in an exam, so questions can be reused across exams.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExamQuestion implements Identifiable {

	@EqualsAndHashCode.Include
	private Long id;

	/** FK -> Exam */
	private Long examId;
	/** FK -> Question */
	private Long questionId;
	private int position;
	/** Points for a correct answer */
	private double answerPoints;
	/** Points for the explanation (scaled from rubric score) */
	private double explanationPoints;
}
