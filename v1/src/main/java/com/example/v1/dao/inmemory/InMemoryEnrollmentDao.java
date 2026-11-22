package com.example.v1.dao.inmemory;

import com.example.v1.dao.EnrollmentDao;
import com.example.v1.model.Enrollment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryEnrollmentDao extends InMemoryCrudDao<Enrollment> implements EnrollmentDao {

	@Override
	public List<Enrollment> findByCourseId(Long courseId) {
		return findWhere(e -> Objects.equals(e.getCourseId(), courseId));
	}

	@Override
	public List<Enrollment> findByStudentId(Long studentId) {
		return findWhere(e -> Objects.equals(e.getStudentId(), studentId));
	}

	@Override
	public boolean existsByCourseIdAndStudentId(Long courseId, Long studentId) {
		return findFirstWhere(e -> Objects.equals(e.getCourseId(), courseId) && Objects.equals(e.getStudentId(), studentId)).isPresent();
	}
}
