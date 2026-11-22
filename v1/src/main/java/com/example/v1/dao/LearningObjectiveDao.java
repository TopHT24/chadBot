package com.example.v1.dao;

import com.example.v1.model.LearningObjective;

import java.util.List;

public interface LearningObjectiveDao extends CrudDao<LearningObjective, Long> {

	List<LearningObjective> findByCourseId(Long courseId);
}
