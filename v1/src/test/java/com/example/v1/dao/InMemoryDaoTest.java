package com.example.v1.dao;

import com.example.v1.dao.inmemory.InMemoryExamDao;
import com.example.v1.dao.inmemory.InMemoryExamQuestionDao;
import com.example.v1.dao.inmemory.InMemoryUserDao;
import com.example.v1.model.Exam;
import com.example.v1.model.ExamQuestion;
import com.example.v1.model.Question;
import com.example.v1.model.User;
import com.example.v1.model.enums.ExamStatus;
import com.example.v1.model.enums.QuestionType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryDaoTest {

	@Test
	void saveAssignsIdsAndFindsByEmail() {
		UserDao users = new InMemoryUserDao();
		User saved = users.save(User.builder().fullName("Ada").email("ada@example.com").build());

		assertThat(saved.getId()).isEqualTo(1L);
		assertThat(saved.getCreatedAt()).isNotNull();
		assertThat(users.findByEmail("ADA@example.com")).contains(saved);
	}

	@Test
	void builderAndNoArgsConstructorApplyDefaults() {
		Question built = Question.builder().type(QuestionType.MULTIPLE_CHOICE).stem("?").build();
		Exam empty = new Exam();

		assertThat(built.getOptions()).isEmpty();
		assertThat(built.getCreatedAt()).isNotNull();
		assertThat(empty.getStatus()).isEqualTo(ExamStatus.GENERATING);
		assertThat(empty.getSourceMaterialIds()).isEmpty();
	}

	@Test
	void findsExamsByOwnerAndOrdersExamQuestionsByPosition() {
		ExamDao exams = new InMemoryExamDao();
		exams.save(Exam.builder().userId(1L).title("Mine").build());
		exams.save(Exam.builder().userId(2L).title("Someone else's").build());
		assertThat(exams.findByUserId(1L)).extracting(Exam::getTitle).containsExactly("Mine");

		ExamQuestionDao examQuestions = new InMemoryExamQuestionDao();
		examQuestions.save(ExamQuestion.builder().examId(1L).questionId(10L).position(2).build());
		examQuestions.save(ExamQuestion.builder().examId(1L).questionId(11L).position(1).build());
		assertThat(examQuestions.findByExamId(1L)).extracting(ExamQuestion::getQuestionId).containsExactly(11L, 10L);
	}
}
