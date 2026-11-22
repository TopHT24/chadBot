package com.example.v1.dao.inmemory;

import com.example.v1.dao.ExamDao;
import com.example.v1.model.Exam;
import com.example.v1.model.enums.ExamStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryExamDao extends InMemoryCrudDao<Exam> implements ExamDao {

	@Override
	public List<Exam> findByCourseId(Long courseId) {
		return findWhere(e -> Objects.equals(e.getCourseId(), courseId));
	}

	@Override
	public List<Exam> findByCourseIdAndStatus(Long courseId, ExamStatus status) {
		return findWhere(e -> Objects.equals(e.getCourseId(), courseId) && e.getStatus() == status);
	}
}
