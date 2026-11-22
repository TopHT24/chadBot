package com.example.v1.dao.inmemory;

import com.example.v1.dao.ExamQuestionDao;
import com.example.v1.model.ExamQuestion;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemoryExamQuestionDao extends InMemoryCrudDao<ExamQuestion> implements ExamQuestionDao {

	@Override
	public List<ExamQuestion> findByExamId(Long examId) {
		return findWhere(eq -> Objects.equals(eq.getExamId(), examId)).stream()
				.sorted(Comparator.comparingInt(ExamQuestion::getPosition))
				.toList();
	}

	@Override
	public Optional<ExamQuestion> findByExamIdAndQuestionId(Long examId, Long questionId) {
		return findFirstWhere(eq -> Objects.equals(eq.getExamId(), examId) && Objects.equals(eq.getQuestionId(), questionId));
	}
}
