package com.example.v1.service.llm;

import com.example.v1.dto.llm.ExplanationGradingInput;
import com.example.v1.dto.llm.ExplanationGradingResult;
import com.example.v1.dto.llm.GradedCriterion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Fake grader with no LLM: a criterion counts as met when the explanation
 * contains its key concept word. Never asks follow-up questions.
 * Replace with a real LLM implementation.
 */
@Slf4j
@Component
public class StubExplanationGrader implements ExplanationGrader {

	public StubExplanationGrader() {
		log.warn("Using StubExplanationGrader: explanations are graded by keyword matching, not an LLM");
	}

	@Override
	public ExplanationGradingResult grade(ExplanationGradingInput input) {
		String explanation = input.studentExplanation() == null ? "" : input.studentExplanation();
		String lower = explanation.toLowerCase(Locale.ROOT);

		List<GradedCriterion> graded = input.criteria().stream()
				.map(c -> {
					String concept = c.keyConcept().toLowerCase(Locale.ROOT);
					boolean met = lower.contains(concept);
					return new GradedCriterion(c.criterionId(), met, met ? sentenceContaining(explanation, concept) : "");
				})
				.toList();

		long metCount = graded.stream().filter(GradedCriterion::met).count();
		String feedback = "[stub grader] Covered " + metCount + " of " + graded.size() + " key concepts.";
		return new ExplanationGradingResult(graded, List.of(), input.answerCorrect(), feedback, 0.9, null);
	}

	private static String sentenceContaining(String text, String concept) {
		return Arrays.stream(text.split("(?<=[.!?])\\s+"))
				.filter(s -> s.toLowerCase(Locale.ROOT).contains(concept))
				.findFirst()
				.orElse(text);
	}
}
