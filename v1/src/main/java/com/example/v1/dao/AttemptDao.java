package com.example.v1.dao;

import com.example.v1.model.Attempt;

import java.util.List;

public interface AttemptDao extends CrudDao<Attempt, Long> {

	List<Attempt> findByExamId(Long examId);

	List<Attempt> findByUserId(Long userId);

	List<Attempt> findByExamIdAndUserId(Long examId, Long userId);

	long countByExamIdAndUserId(Long examId, Long userId);
}
