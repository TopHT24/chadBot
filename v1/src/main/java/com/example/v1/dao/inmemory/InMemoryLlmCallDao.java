package com.example.v1.dao.inmemory;

import com.example.v1.dao.LlmCallDao;
import com.example.v1.model.LlmCall;
import com.example.v1.model.enums.PromptPurpose;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryLlmCallDao extends InMemoryCrudDao<LlmCall> implements LlmCallDao {

	@Override
	public List<LlmCall> findByPromptTemplateId(Long promptTemplateId) {
		return findWhere(c -> Objects.equals(c.getPromptTemplateId(), promptTemplateId));
	}

	@Override
	public List<LlmCall> findByPurpose(PromptPurpose purpose) {
		return findWhere(c -> c.getPurpose() == purpose);
	}
}
