package com.example.v1.dao.inmemory;

import com.example.v1.dao.GradeOverrideDao;
import com.example.v1.model.GradeOverride;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryGradeOverrideDao extends InMemoryCrudDao<GradeOverride> implements GradeOverrideDao {

	@Override
	public List<GradeOverride> findByEvaluationId(Long evaluationId) {
		return findWhere(g -> Objects.equals(g.getEvaluationId(), evaluationId));
	}
}
