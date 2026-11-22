package com.example.v1.dto.llm;

/**
 * @param evidenceQuote exact words from the student's explanation; empty when not met
 */
public record GradedCriterion(Long criterionId, boolean met, String evidenceQuote) {
}
