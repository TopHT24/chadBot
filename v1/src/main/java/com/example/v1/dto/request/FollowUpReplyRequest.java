package com.example.v1.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FollowUpReplyRequest(@NotBlank String reply) {
}
