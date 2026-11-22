package com.example.v1.dao.inmemory;

import com.example.v1.dao.StudentAnswerDao;
import com.example.v1.model.StudentAnswer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemoryStudentAnswerDao extends InMemoryCrudDao<StudentAnswer> implements StudentAnswerDao {

	@Override
	public List<StudentAnswer> findByAttemptId(Long attemptId) {
		return findWhere(a -> Objects.equals(a.getAttemptId(), attemptId));
	}

	@Override
	public Optional<StudentAnswer> findByAttemptIdAndQuestionId(Long attemptId, Long questionId) {
		return findFirstWhere(a -> Objects.equals(a.getAttemptId(), attemptId) && Objects.equals(a.getQuestionId(), questionId));
	}
}
