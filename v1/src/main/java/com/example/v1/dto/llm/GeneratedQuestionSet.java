package com.example.v1.dto.llm;

import java.util.List;

/**
 * Top-level response for a QUESTION_GENERATION call.
 */
public record GeneratedQuestionSet(List<GeneratedQuestion> questions) {
}
