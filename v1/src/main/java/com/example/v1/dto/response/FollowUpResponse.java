package com.example.v1.dto.response;

import com.example.v1.model.FollowUp;

import java.time.Instant;

public record FollowUpResponse(Long id, Long studentAnswerId, int round, String question, String studentReply,
		Instant askedAt, Instant repliedAt) {

	public static FollowUpResponse from(FollowUp f) {
		return new FollowUpResponse(f.getId(), f.getStudentAnswerId(), f.getRound(), f.getQuestion(),
				f.getStudentReply(), f.getAskedAt(), f.getRepliedAt());
	}
}
