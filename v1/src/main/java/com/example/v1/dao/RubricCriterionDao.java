package com.example.v1.dao;

import com.example.v1.model.RubricCriterion;

import java.util.List;

public interface RubricCriterionDao extends CrudDao<RubricCriterion, Long> {

	List<RubricCriterion> findByRubricId(Long rubricId);
}
