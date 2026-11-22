package com.example.v1.dto.response;

import com.example.v1.model.Course;

import java.time.Instant;

public record CourseResponse(Long id, String name, String description, Long teacherId, Instant createdAt) {

	public static CourseResponse from(Course c) {
		return new CourseResponse(c.getId(), c.getName(), c.getDescription(), c.getTeacherId(), c.getCreatedAt());
	}
}
