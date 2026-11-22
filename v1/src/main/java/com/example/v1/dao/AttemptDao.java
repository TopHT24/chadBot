package com.example.v1.dao;

import com.example.v1.model.Attempt;

import java.util.List;

public interface AttemptDao extends CrudDao<Attempt, Long> {

	List<Attempt> findByExamId(Long examId);

	List<Attempt> findByStudentId(Long studentId);

	List<Attempt> findByExamIdAndStudentId(Long examId, Long studentId);

	long countByExamIdAndStudentId(Long examId, Long studentId);
}
