package com.example.v1.dto.response;

import com.example.v1.model.Rubric;
import com.example.v1.model.RubricCriterion;

import java.util.List;

/**
 * Teacher-only.
 */
public record RubricResponse(Long id, String referenceExplanation, List<Criterion> criteria,
		List<String> commonMisconceptions, double maxPoints) {

	public record Criterion(Long id, String keyConcept, String description, double points, boolean required) {

		public static Criterion from(RubricCriterion c) {
			return new Criterion(c.getId(), c.getKeyConcept(), c.getDescription(), c.getPoints(), c.isRequired());
		}
	}

	public static RubricResponse from(Rubric r, List<RubricCriterion> criteria) {
		List<Criterion> mapped = criteria.stream().map(Criterion::from).toList();
		double max = mapped.stream().mapToDouble(Criterion::points).sum();
		return new RubricResponse(r.getId(), r.getReferenceExplanation(), mapped,
				List.copyOf(r.getCommonMisconceptions()), max);
	}
}
