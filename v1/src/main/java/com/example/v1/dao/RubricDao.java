package com.example.v1.dao;

import com.example.v1.model.Rubric;

import java.util.List;
import java.util.Optional;

public interface RubricDao extends CrudDao<Rubric, Long> {

	Optional<Rubric> findByQuestionId(Long questionId);
}
