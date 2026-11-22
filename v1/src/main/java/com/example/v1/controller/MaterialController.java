package com.example.v1.controller;

import com.example.v1.dto.request.CreateSourceMaterialRequest;
import com.example.v1.dto.response.SourceMaterialResponse;
import jakarta.validation.Valid;
import com.example.v1.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * The student's study material: files uploaded from their computer, or pasted text.
 * Tests are generated from these.
 */
@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

	private final MaterialService materialService;

	/** Upload a file (PDF, DOCX, TXT...) from the student's computer. */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public SourceMaterialResponse upload(@RequestPart("file") MultipartFile file,
			@RequestParam Long userId,
			@RequestParam(required = false) String title) {
		return materialService.upload(file, userId, title);
	}

	/** Paste text instead of uploading a file. */
	@PostMapping(path = "/text", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public SourceMaterialResponse addText(@Valid @RequestBody CreateSourceMaterialRequest request) {
		return materialService.addText(request);
	}

	/** The student's materials. */
	@GetMapping
	public List<SourceMaterialResponse> list(@RequestParam Long userId) {
		return materialService.findByUser(userId);
	}

	@GetMapping("/{id}")
	public SourceMaterialResponse get(@PathVariable Long id) {
		return materialService.get(id);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		materialService.delete(id);
	}
}
