package com.example.v1.dao;

import com.example.v1.model.Course;

import java.util.List;

public interface CourseDao extends CrudDao<Course, Long> {

	List<Course> findByTeacherId(Long teacherId);
}
