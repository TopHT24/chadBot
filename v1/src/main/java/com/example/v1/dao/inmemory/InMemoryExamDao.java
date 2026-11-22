package com.example.v1.dao.inmemory;

import com.example.v1.dao.ExamDao;
import com.example.v1.model.Exam;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryExamDao extends InMemoryCrudDao<Exam> implements ExamDao {

	@Override
	public List<Exam> findByUserId(Long userId) {
		return findWhere(e -> Objects.equals(e.getUserId(), userId));
	}
}
