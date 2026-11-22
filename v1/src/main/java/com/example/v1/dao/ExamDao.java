package com.example.v1.dao;

import com.example.v1.model.Exam;

import java.util.List;

public interface ExamDao extends CrudDao<Exam, Long> {

	List<Exam> findByUserId(Long userId);
}
