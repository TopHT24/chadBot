package com.example.v1.dao;

import com.example.v1.model.ExamQuestion;

import java.util.List;
import java.util.Optional;

public interface ExamQuestionDao extends CrudDao<ExamQuestion, Long> {

	/** Ordered by position. */
	List<ExamQuestion> findByExamId(Long examId);

	Optional<ExamQuestion> findByExamIdAndQuestionId(Long examId, Long questionId);
}
