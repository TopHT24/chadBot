package com.example.v1.dao.inmemory;

import com.example.v1.dao.LearningObjectiveDao;
import com.example.v1.model.LearningObjective;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryLearningObjectiveDao extends InMemoryCrudDao<LearningObjective> implements LearningObjectiveDao {

	@Override
	public List<LearningObjective> findByCourseId(Long courseId) {
		return findWhere(o -> Objects.equals(o.getCourseId(), courseId));
	}
}
