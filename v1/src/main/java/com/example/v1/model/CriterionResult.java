package com.example.v1.model;

/**
 * Value object inside an ExplanationEvaluation: how the explanation did on one rubric criterion.
 * Not an entity of its own; stored as JSONB later.
 *
 * @param criterionId   FK -> RubricCriterion
 * @param met           whether the explanation satisfies the criterion (decided by the LLM)
 * @param evidenceQuote the part of the student's explanation that justifies the decision
 * @param pointsAwarded computed in code from the criterion's points, not by the LLM
 */
public record CriterionResult(Long criterionId, boolean met, String evidenceQuote, double pointsAwarded) {
}
