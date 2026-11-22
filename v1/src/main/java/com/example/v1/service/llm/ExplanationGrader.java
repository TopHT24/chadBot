package com.example.v1.service.llm;

import com.example.v1.dto.llm.ExplanationGradingInput;
import com.example.v1.dto.llm.ExplanationGradingResult;

/**
 * Grades a student's explanation against a rubric.
 * The real implementation will call the LLM API; StubExplanationGrader is used until then.
 */
public interface ExplanationGrader {

	ExplanationGradingResult grade(ExplanationGradingInput input);
}
