package com.example.v1.dao.inmemory;

import com.example.v1.dao.CourseDao;
import com.example.v1.model.Course;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryCourseDao extends InMemoryCrudDao<Course> implements CourseDao {

	@Override
	public List<Course> findByTeacherId(Long teacherId) {
		return findWhere(c -> Objects.equals(c.getTeacherId(), teacherId));
	}
}
