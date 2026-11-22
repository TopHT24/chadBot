package com.example.v1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
		@NotBlank String fullName,
		@NotBlank @Email String email) {
}
