package com.example.v1.dao;

import com.example.v1.model.Enrollment;

import java.util.List;

public interface EnrollmentDao extends CrudDao<Enrollment, Long> {

	List<Enrollment> findByCourseId(Long courseId);

	List<Enrollment> findByStudentId(Long studentId);

	boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);
}
