package com.example.v1.dto.response;

import com.example.v1.model.User;
import com.example.v1.model.enums.Role;

import java.time.Instant;

public record UserResponse(Long id, String fullName, String email, Role role, Instant createdAt) {

	public static UserResponse from(User u) {
		return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getRole(), u.getCreatedAt());
	}
}
