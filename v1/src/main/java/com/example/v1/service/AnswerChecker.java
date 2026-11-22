package com.example.v1.service;

import com.example.v1.model.Question;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Grades the answer itself, in code. The LLM only grades explanations.
 */
@Component
public class AnswerChecker {

	private static final double NUMERIC_TOLERANCE = 1e-6;

	/** @return true/false, or null for OPEN questions, which have no single right answer */
	public Boolean check(Question question, String given) {
		if (given == null || given.isBlank()) {
			return false;
		}
		String expected = question.getCorrectAnswer();
		return switch (question.getType()) {
			case MULTIPLE_CHOICE, TRUE_FALSE, SHORT_ANSWER -> normalize(given).equals(normalize(expected));
			case NUMERIC -> numericEquals(given, expected);
			case OPEN -> null;
		};
	}

	static String normalize(String s) {
		return s == null ? "" : s.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ").replaceAll("[.!]$", "");
	}

	private static boolean numericEquals(String given, String expected) {
		try {
			return Math.abs(Double.parseDouble(given.trim()) - Double.parseDouble(expected.trim())) <= NUMERIC_TOLERANCE;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
