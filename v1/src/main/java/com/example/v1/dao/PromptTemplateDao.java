package com.example.v1.dao;

import com.example.v1.model.PromptTemplate;
import com.example.v1.model.enums.PromptPurpose;

import java.util.List;
import java.util.Optional;

public interface PromptTemplateDao extends CrudDao<PromptTemplate, Long> {

	Optional<PromptTemplate> findActiveByName(String name);

	Optional<PromptTemplate> findByNameAndVersion(String name, int version);

	List<PromptTemplate> findByPurpose(PromptPurpose purpose);
}
