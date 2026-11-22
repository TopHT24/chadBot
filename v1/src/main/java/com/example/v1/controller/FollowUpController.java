package com.example.v1.controller;

import com.example.v1.dto.request.FollowUpReplyRequest;
import com.example.v1.dto.response.FollowUpResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * When an explanation is too vague, the LLM asks a follow-up question and the
 * student must reply: this is what forces students to really explain.
 */
@RestController
@RequestMapping("/api/follow-ups")
public class FollowUpController {

	/** Follow-up questions asked about one answer. */
	@GetMapping
	public List<FollowUpResponse> list(@RequestParam Long studentAnswerId) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented yet");
	}

	/** The student replies to a follow-up question. */
	@PostMapping("/{id}/reply")
	public FollowUpResponse reply(@PathVariable Long id, @Valid @RequestBody FollowUpReplyRequest request) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented yet");
	}
}
