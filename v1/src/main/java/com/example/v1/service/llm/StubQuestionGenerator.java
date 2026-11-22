package com.example.v1.service.llm;

import com.example.v1.dto.llm.GeneratedCriterion;
import com.example.v1.dto.llm.GeneratedQuestion;
import com.example.v1.dto.llm.GeneratedQuestionSet;
import com.example.v1.dto.llm.QuestionGenerationInput;
import com.example.v1.model.enums.BloomLevel;
import com.example.v1.model.enums.Difficulty;
import com.example.v1.model.enums.QuestionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Fake generator with no LLM: turns sentences of the material into TRUE_FALSE
 * questions, and uses each sentence's longest words as rubric key concepts.
 * Ignores the requested question types. Replace with a real LLM implementation.
 */
@Slf4j
@Component
public class StubQuestionGenerator implements QuestionGenerator {

	private static final int MIN_SENTENCE_LENGTH = 30;

	public StubQuestionGenerator() {
		log.warn("Using StubQuestionGenerator: questions are generated without an LLM");
	}

	@Override
	public GeneratedQuestionSet generate(QuestionGenerationInput input) {
		Difficulty difficulty = input.difficulty() == null ? Difficulty.MEDIUM : input.difficulty();
		List<GeneratedQuestion> questions = Arrays.stream(input.sourceText().split("(?<=[.!?])\\s+"))
				.map(String::trim)
				.filter(s -> s.length() >= MIN_SENTENCE_LENGTH)
				.limit(input.count())
				.map(sentence -> new GeneratedQuestion(
						QuestionType.TRUE_FALSE,
						"True or false: " + sentence,
						List.of("True", "False"),
						"True",
						"Explain why this statement is true or false.",
						difficulty,
						BloomLevel.UNDERSTAND,
						sentence,
						criteriaFor(sentence),
						List.of()))
				.toList();
		return new GeneratedQuestionSet(questions);
	}

	private static List<GeneratedCriterion> criteriaFor(String sentence) {
		List<String> keyWords = Arrays.stream(sentence.split("[^\\p{L}]+"))
				.filter(w -> w.length() >= 5)
				.map(w -> w.toLowerCase(Locale.ROOT))
				.distinct()
				.sorted(Comparator.comparingInt(String::length).reversed())
				.limit(2)
				.toList();
		return keyWords.stream()
				.map(w -> new GeneratedCriterion(w, "Mentions \"" + w + "\"", 1.0, w.equals(keyWords.getFirst())))
				.toList();
	}
}
