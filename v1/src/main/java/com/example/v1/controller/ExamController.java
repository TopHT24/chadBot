package com.example.v1.controller;

import com.example.v1.dto.request.GenerateExamRequest;
import com.example.v1.dto.response.ExamResponse;
import com.example.v1.dto.response.StudentExamView;
import jakarta.validation.Valid;
import com.example.v1.service.AttemptService;
import com.example.v1.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Tests generated from the student's materials. ("Exam" avoids clashing with JUnit's @Test.)
 * Answering happens in an attempt: see AttemptController.
 */
@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

	private final ExamService examService;
	private final AttemptService attemptService;

	/** Generate a new test from some of the student's materials. */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ExamResponse generate(@Valid @RequestBody GenerateExamRequest request) {
		return examService.generate(request);
	}

	/** The student's tests. */
	@GetMapping
	public List<ExamResponse> list(@RequestParam Long userId) {
		return examService.findByUser(userId);
	}

	/** One test: check whether generation is done (status READY). */
	@GetMapping("/{id}")
	public ExamResponse get(@PathVariable Long id) {
		return examService.get(id);
	}

	/** Start (or retake) the test. Returns the questions without correct answers. */
	@PostMapping("/{id}/attempts")
	@ResponseStatus(HttpStatus.CREATED)
	public StudentExamView startAttempt(@PathVariable Long id, @RequestParam Long userId) {
		return attemptService.start(id, userId);
	}
}
