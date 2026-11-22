package com.example.v1.dao.inmemory;

import com.example.v1.dao.PromptTemplateDao;
import com.example.v1.model.PromptTemplate;
import com.example.v1.model.enums.PromptPurpose;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemoryPromptTemplateDao extends InMemoryCrudDao<PromptTemplate> implements PromptTemplateDao {

	@Override
	public Optional<PromptTemplate> findActiveByName(String name) {
		return findFirstWhere(p -> p.isActive() && Objects.equals(p.getName(), name));
	}

	@Override
	public Optional<PromptTemplate> findByNameAndVersion(String name, int version) {
		return findFirstWhere(p -> Objects.equals(p.getName(), name) && p.getVersion() == version);
	}

	@Override
	public List<PromptTemplate> findByPurpose(PromptPurpose purpose) {
		return findWhere(p -> p.getPurpose() == purpose);
	}
}
