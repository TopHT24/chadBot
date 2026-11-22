package com.example.v1.dao.inmemory;

import com.example.v1.dao.FollowUpDao;
import com.example.v1.model.FollowUp;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryFollowUpDao extends InMemoryCrudDao<FollowUp> implements FollowUpDao {

	@Override
	public List<FollowUp> findByStudentAnswerId(Long studentAnswerId) {
		return findWhere(f -> Objects.equals(f.getStudentAnswerId(), studentAnswerId)).stream()
				.sorted(Comparator.comparingInt(FollowUp::getRound))
				.toList();
	}
}
