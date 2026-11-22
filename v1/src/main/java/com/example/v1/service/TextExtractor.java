package com.example.v1.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Set;

/**
 * Gets the text out of an uploaded file, so it can be sent to the LLM.
 * Plain-text files only for now; PDF and Word support is the next step.
 */
@Component
public class TextExtractor {

	private static final Set<String> SUPPORTED = Set.of("txt", "md", "csv");

	public String extract(MultipartFile file) {
		String extension = extensionOf(file.getOriginalFilename());
		if (!SUPPORTED.contains(extension)) {
			throw new BadRequestException("Only .txt, .md and .csv files are supported for now (got \""
					+ file.getOriginalFilename() + "\")");
		}
		try {
			String text = new String(file.getBytes(), StandardCharsets.UTF_8).strip();
			if (text.isEmpty()) {
				throw new BadRequestException("The file is empty");
			}
			return text;
		} catch (IOException e) {
			throw new BadRequestException("Could not read the file");
		}
	}

	static String extensionOf(String filename) {
		if (filename == null || filename.lastIndexOf('.') < 0) {
			return "";
		}
		return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
	}
}
