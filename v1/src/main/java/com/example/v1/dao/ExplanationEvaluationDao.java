package com.example.v1.dao;

import com.example.v1.model.ExplanationEvaluation;

import java.util.List;
import java.util.Optional;

public interface ExplanationEvaluationDao extends CrudDao<ExplanationEvaluation, Long> {

	/** Most recent evaluation for an answer (re-grading creates new rows). */
	Optional<ExplanationEvaluation> findLatestByStudentAnswerId(Long studentAnswerId);

	List<ExplanationEvaluation> findByStudentAnswerId(Long studentAnswerId);
}
