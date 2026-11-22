package com.example.v1.controller;

import com.example.v1.dto.request.SubmitAnswerRequest;
import com.example.v1.dto.response.AttemptResponse;
import com.example.v1.dto.response.AttemptResultResponse;
import com.example.v1.dto.response.StudentAnswerResponse;
import com.example.v1.dto.response.StudentExamView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Taking a test: answer with explanations, submit, get feedback.
 */
@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

	/** The student's attempts, with status and scores (their history). */
	@GetMapping
	public List<AttemptResponse> list(@RequestParam Long userId) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented yet");
	}

	/** Resume an attempt in progress. */
	@GetMapping("/{id}")
	public StudentExamView get(@PathVariable Long id) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented yet");
	}

	/** Save the answer AND explanation for one question. Can be called again to revise. */
	@PutMapping("/{id}/answers")
	public StudentAnswerResponse answer(@PathVariable Long id, @Valid @RequestBody SubmitAnswerRequest request) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented yet");
	}

	/** Finish: grades answers and explanations, returns the feedback. */
	@PostMapping("/{id}/submit")
	public AttemptResultResponse submit(@PathVariable Long id) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented yet");
	}

	/** See the feedback of a submitted attempt again. */
	@GetMapping("/{id}/result")
	public AttemptResultResponse result(@PathVariable Long id) {
		throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Not implemented yet");
	}
}
