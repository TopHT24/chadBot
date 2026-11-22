package com.example.v1.dao.inmemory;

import com.example.v1.dao.RubricDao;
import com.example.v1.model.Rubric;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemoryRubricDao extends InMemoryCrudDao<Rubric> implements RubricDao {

	@Override
	public Optional<Rubric> findByQuestionId(Long questionId) {
		return findFirstWhere(r -> Objects.equals(r.getQuestionId(), questionId));
	}
}
