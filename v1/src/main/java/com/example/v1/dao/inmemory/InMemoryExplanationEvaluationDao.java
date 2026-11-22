package com.example.v1.dao.inmemory;

import com.example.v1.dao.ExplanationEvaluationDao;
import com.example.v1.model.ExplanationEvaluation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemoryExplanationEvaluationDao extends InMemoryCrudDao<ExplanationEvaluation> implements ExplanationEvaluationDao {

	@Override
	public Optional<ExplanationEvaluation> findLatestByStudentAnswerId(Long studentAnswerId) {
		List<ExplanationEvaluation> all = findByStudentAnswerId(studentAnswerId);
		return all.isEmpty() ? Optional.empty() : Optional.of(all.getLast());
	}

	@Override
	public List<ExplanationEvaluation> findByStudentAnswerId(Long studentAnswerId) {
		return findWhere(e -> Objects.equals(e.getStudentAnswerId(), studentAnswerId));
	}
}
