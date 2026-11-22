package com.example.v1.dto.request;

import com.example.v1.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
		@NotBlank String fullName,
		@NotBlank @Email String email,
		@NotNull Role role) {
}
