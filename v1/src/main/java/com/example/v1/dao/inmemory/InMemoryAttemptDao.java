package com.example.v1.dao.inmemory;

import com.example.v1.dao.AttemptDao;
import com.example.v1.model.Attempt;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryAttemptDao extends InMemoryCrudDao<Attempt> implements AttemptDao {

	@Override
	public List<Attempt> findByExamId(Long examId) {
		return findWhere(a -> Objects.equals(a.getExamId(), examId));
	}

	@Override
	public List<Attempt> findByUserId(Long userId) {
		return findWhere(a -> Objects.equals(a.getUserId(), userId));
	}

	@Override
	public List<Attempt> findByExamIdAndUserId(Long examId, Long userId) {
		return findWhere(a -> Objects.equals(a.getExamId(), examId) && Objects.equals(a.getUserId(), userId));
	}

	@Override
	public long countByExamIdAndUserId(Long examId, Long userId) {
		return findByExamIdAndUserId(examId, userId).size();
	}
}
