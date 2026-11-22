package com.example.v1.dao;

import com.example.v1.model.StudentAnswer;

import java.util.List;
import java.util.Optional;

public interface StudentAnswerDao extends CrudDao<StudentAnswer, Long> {

	List<StudentAnswer> findByAttemptId(Long attemptId);

	Optional<StudentAnswer> findByAttemptIdAndQuestionId(Long attemptId, Long questionId);
}
