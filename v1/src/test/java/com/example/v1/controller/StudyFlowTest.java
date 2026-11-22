package com.example.v1.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The whole student flow over HTTP: upload a file -> generate a test -> answer
 * with explanations -> graded feedback. Uses the stub LLM classes.
 */
@SpringBootTest
@AutoConfigureMockMvc
class StudyFlowTest {

	private static final long USER_ID = 100L; // own id, so other tests' data doesn't interfere

	@Autowired
	private MockMvc mvc;

	@Test
	void studentUploadsNotesAndIsGradedOnExplanations() throws Exception {
		// 1. upload notes from the computer
		MockMultipartFile notes = new MockMultipartFile("file", "photosynthesis.txt", "text/plain",
				"Plants use chlorophyll to absorb sunlight during photosynthesis. Photosynthesis releases oxygen as a byproduct."
						.getBytes());
		String material = mvc.perform(multipart("/api/materials").file(notes).param("userId", String.valueOf(USER_ID)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("photosynthesis"))
				.andReturn().getResponse().getContentAsString();
		long materialId = id(material, "$.id");

		// 2. generate a test from it
		String exam = postJson("/api/exams", """
				{"userId": %d, "sourceMaterialIds": [%d], "questionCount": 2}
				""".formatted(USER_ID, materialId));
		long examId = id(exam, "$.id");
		mvc.perform(get("/api/exams/" + examId))
				.andExpect(jsonPath("$.status").value("READY"))
				.andExpect(jsonPath("$.questionCount").value(2));

		// 3. start it: questions come without correct answers or rubrics
		String view = mvc.perform(post("/api/exams/" + examId + "/attempts").param("userId", String.valueOf(USER_ID)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.questions", hasSize(2)))
				.andExpect(content().string(not(containsString("correctAnswer"))))
				.andExpect(content().string(not(containsString("referenceExplanation"))))
				.andReturn().getResponse().getContentAsString();
		long attemptId = id(view, "$.attemptId");
		long q1 = id(view, "$.questions[0].questionId");
		long q2 = id(view, "$.questions[1].questionId");

		// 4. a too-short explanation is refused
		mvc.perform(put("/api/attempts/" + attemptId + "/answers").contentType(MediaType.APPLICATION_JSON)
						.content("{\"questionId\": %d, \"answer\": \"True\", \"explanation\": \"Because.\"}".formatted(q1)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail", containsString("at least 15 words")));

		// 5. q1: right answer, explanation names the key concepts; q2: wrong answer, vague explanation
		answer(attemptId, q1, "True",
				"Plants contain chlorophyll which can absorb sunlight, and that light energy is what drives photosynthesis in the leaves");
		answer(attemptId, q2, "False",
				"I do not really think plants give off any kind of gas at all while they are making their own food");

		// 6. submit and check the feedback
		mvc.perform(post("/api/attempts/" + attemptId + "/submit"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.attempt.status").value("GRADED"))
				.andExpect(jsonPath("$.results[0].answerCorrect").value(true))
				.andExpect(jsonPath("$.results[0].explanationPointsEarned").value(2.0))
				.andExpect(jsonPath("$.results[0].modelExplanation", containsString("chlorophyll")))
				.andExpect(jsonPath("$.results[1].answerCorrect").value(false))
				.andExpect(jsonPath("$.results[1].explanationPointsEarned").value(0.0))
				.andExpect(jsonPath("$.attempt.totalScore").value(3.0));

		// 7. retake: a new attempt is created
		mvc.perform(post("/api/exams/" + examId + "/attempts").param("userId", String.valueOf(USER_ID)))
				.andExpect(jsonPath("$.attemptId", not(attemptId)));
	}

	@Test
	void pdfIsRefusedForNow() throws Exception {
		MockMultipartFile pdf = new MockMultipartFile("file", "notes.pdf", "application/pdf", new byte[] {1, 2, 3});
		mvc.perform(multipart("/api/materials").file(pdf).param("userId", "1"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail", containsString("supported")));
	}

	@Test
	void cannotUseSomeoneElsesMaterial() throws Exception {
		long materialId = id(postJson("/api/materials/text",
				"{\"userId\": 1, \"title\": \"Mine\", \"content\": \"Some long enough sentence about the water cycle and rain.\"}"), "$.id");
		mvc.perform(post("/api/exams").contentType(MediaType.APPLICATION_JSON)
						.content("{\"userId\": 2, \"sourceMaterialIds\": [%d], \"questionCount\": 1}".formatted(materialId)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail", containsString("another user")));
	}

	@Test
	void materialWithoutUsableSentencesGivesFailedExam() throws Exception {
		long materialId = id(postJson("/api/materials/text",
				"{\"userId\": 3, \"title\": \"Short\", \"content\": \"Too short.\"}"), "$.id");
		postJsonExpect("/api/exams", "{\"userId\": 3, \"sourceMaterialIds\": [%d], \"questionCount\": 1}".formatted(materialId))
				.andExpect(jsonPath("$.status").value("FAILED"))
				.andExpect(jsonPath("$.errorMessage", containsString("No usable questions")));
	}

	@Test
	void errorsAreJson() throws Exception {
		mvc.perform(get("/api/exams/999999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.detail").value("Exam 999999 not found"));
		mvc.perform(post("/api/exams").contentType(MediaType.APPLICATION_JSON).content("{}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", not(hasSize(0))));
	}

	private void answer(long attemptId, long questionId, String answer, String explanation) throws Exception {
		mvc.perform(put("/api/attempts/" + attemptId + "/answers").contentType(MediaType.APPLICATION_JSON)
						.content("{\"questionId\": %d, \"answer\": \"%s\", \"explanation\": \"%s\"}"
								.formatted(questionId, answer, explanation)))
				.andExpect(status().isOk());
	}

	private String postJson(String url, String json) throws Exception {
		return postJsonExpect(url, json).andReturn().getResponse().getContentAsString();
	}

	private org.springframework.test.web.servlet.ResultActions postJsonExpect(String url, String json) throws Exception {
		return mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated());
	}

	private static long id(String json, String path) {
		return ((Number) JsonPath.read(json, path)).longValue();
	}
}
