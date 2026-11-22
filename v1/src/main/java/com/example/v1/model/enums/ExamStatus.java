package com.example.v1.model.enums;

public enum ExamStatus {
	/** The LLM is generating the questions. */
	GENERATING,
	/** Questions are ready: the student can start an attempt. */
	READY,
	/** Generation failed: see Exam.errorMessage. */
	FAILED
}
