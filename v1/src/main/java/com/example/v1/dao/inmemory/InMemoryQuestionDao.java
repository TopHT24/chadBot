package com.example.v1.dao.inmemory;

import com.example.v1.dao.QuestionDao;
import com.example.v1.model.Question;
import com.example.v1.model.enums.QuestionStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryQuestionDao extends InMemoryCrudDao<Question> implements QuestionDao {

	@Override
	public List<Question> findByCourseId(Long courseId) {
		return findWhere(q -> Objects.equals(q.getCourseId(), courseId));
	}

	@Override
	public List<Question> findByObjectiveId(Long objectiveId) {
		return findWhere(q -> Objects.equals(q.getObjectiveId(), objectiveId));
	}

	@Override
	public List<Question> findByCourseIdAndStatus(Long courseId, QuestionStatus status) {
		return findWhere(q -> Objects.equals(q.getCourseId(), courseId) && q.getStatus() == status);
	}
}
