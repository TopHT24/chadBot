package com.example.v1.dao;

import com.example.v1.model.Exam;
import com.example.v1.model.enums.ExamStatus;

import java.util.List;

public interface ExamDao extends CrudDao<Exam, Long> {

	List<Exam> findByCourseId(Long courseId);

	List<Exam> findByCourseIdAndStatus(Long courseId, ExamStatus status);
}
