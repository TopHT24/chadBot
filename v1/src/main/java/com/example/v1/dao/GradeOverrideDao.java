package com.example.v1.dao;

import com.example.v1.model.GradeOverride;

import java.util.List;

public interface GradeOverrideDao extends CrudDao<GradeOverride, Long> {

	/** Ordered oldest first. */
	List<GradeOverride> findByEvaluationId(Long evaluationId);
}
