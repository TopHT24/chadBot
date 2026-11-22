package com.example.v1.dao;

import com.example.v1.dao.inmemory.InMemoryExamQuestionDao;
import com.example.v1.dao.inmemory.InMemoryQuestionDao;
import com.example.v1.dao.inmemory.InMemoryUserDao;
import com.example.v1.model.ExamQuestion;
import com.example.v1.model.Question;
import com.example.v1.model.User;
import com.example.v1.model.enums.QuestionStatus;
import com.example.v1.model.enums.QuestionType;
import com.example.v1.model.enums.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryDaoTest {

	@Test
	void saveAssignsIdsAndFindsByCustomQueries() {
		UserDao users = new InMemoryUserDao();
		User saved = users.save(User.builder().fullName("Ada").email("ada@example.com").role(Role.STUDENT).build());

		assertThat(saved.getId()).isEqualTo(1L);
		assertThat(saved.getCreatedAt()).isNotNull();
		assertThat(users.findByEmail("ADA@example.com")).contains(saved);
		assertThat(users.findByRole(Role.TEACHER)).isEmpty();
	}

	@Test
	void builderAndNoArgsConstructorApplyDefaults() {
		Question built = Question.builder().courseId(1L).type(QuestionType.MULTIPLE_CHOICE).stem("?").build();
		Question empty = new Question();

		assertThat(built.getStatus()).isEqualTo(QuestionStatus.DRAFT);
		assertThat(built.getOptions()).isEmpty();
		assertThat(empty.getStatus()).isEqualTo(QuestionStatus.DRAFT);
		assertThat(empty.isRequiresExplanation()).isTrue();
	}

	@Test
	void filtersByStatusAndOrdersExamQuestionsByPosition() {
		QuestionDao questions = new InMemoryQuestionDao();
		questions.save(Question.builder().courseId(1L).build());
		questions.save(Question.builder().courseId(1L).status(QuestionStatus.APPROVED).build());
		assertThat(questions.findByCourseIdAndStatus(1L, QuestionStatus.APPROVED)).hasSize(1);

		ExamQuestionDao examQuestions = new InMemoryExamQuestionDao();
		examQuestions.save(ExamQuestion.builder().examId(1L).questionId(10L).position(2).build());
		examQuestions.save(ExamQuestion.builder().examId(1L).questionId(11L).position(1).build());
		assertThat(examQuestions.findByExamId(1L)).extracting(ExamQuestion::getQuestionId).containsExactly(11L, 10L);
	}
}
