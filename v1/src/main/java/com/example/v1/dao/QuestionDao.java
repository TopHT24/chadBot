package com.example.v1.dao;

import com.example.v1.model.Question;
import com.example.v1.model.enums.QuestionStatus;

import java.util.List;

public interface QuestionDao extends CrudDao<Question, Long> {

	List<Question> findByCourseId(Long courseId);

	List<Question> findByObjectiveId(Long objectiveId);

	List<Question> findByCourseIdAndStatus(Long courseId, QuestionStatus status);
}
