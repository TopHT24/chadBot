package com.example.v1.dao.inmemory;

import com.example.v1.dao.RubricCriterionDao;
import com.example.v1.model.RubricCriterion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryRubricCriterionDao extends InMemoryCrudDao<RubricCriterion> implements RubricCriterionDao {

	@Override
	public List<RubricCriterion> findByRubricId(Long rubricId) {
		return findWhere(c -> Objects.equals(c.getRubricId(), rubricId));
	}
}
