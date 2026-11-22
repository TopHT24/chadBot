package com.example.v1.dao.inmemory;

import com.example.v1.dao.SourceMaterialDao;
import com.example.v1.model.SourceMaterial;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InMemorySourceMaterialDao extends InMemoryCrudDao<SourceMaterial> implements SourceMaterialDao {

	@Override
	public List<SourceMaterial> findByCourseId(Long courseId) {
		return findWhere(s -> Objects.equals(s.getCourseId(), courseId));
	}
}
