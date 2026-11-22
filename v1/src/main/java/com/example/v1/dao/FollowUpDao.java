package com.example.v1.dao;

import com.example.v1.model.FollowUp;

import java.util.List;

public interface FollowUpDao extends CrudDao<FollowUp, Long> {

	/** Ordered by round. */
	List<FollowUp> findByStudentAnswerId(Long studentAnswerId);
}
