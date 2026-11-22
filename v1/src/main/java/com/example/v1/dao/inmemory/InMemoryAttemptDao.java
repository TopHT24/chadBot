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
	public List<Attempt> findByStudentId(Long studentId) {
		return findWhere(a -> Objects.equals(a.getStudentId(), studentId));
	}

	@Override
	public List<Attempt> findByExamIdAndStudentId(Long examId, Long studentId) {
		return findWhere(a -> Objects.equals(a.getExamId(), examId) && Objects.equals(a.getStudentId(), studentId));
	}

	@Override
	public long countByExamIdAndStudentId(Long examId, Long studentId) {
		return findByExamIdAndStudentId(examId, studentId).size();
	}
}
