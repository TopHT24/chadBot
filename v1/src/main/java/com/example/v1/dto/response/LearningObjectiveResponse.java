package com.example.v1.dto.response;

import com.example.v1.model.LearningObjective;

public record LearningObjectiveResponse(Long id, Long courseId, String description) {

	public static LearningObjectiveResponse from(LearningObjective o) {
		return new LearningObjectiveResponse(o.getId(), o.getCourseId(), o.getDescription());
	}
}
