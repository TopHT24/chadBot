package com.example.v1.dao;

import com.example.v1.model.LlmCall;
import com.example.v1.model.enums.PromptPurpose;

import java.util.List;

public interface LlmCallDao extends CrudDao<LlmCall, Long> {

	List<LlmCall> findByPromptTemplateId(Long promptTemplateId);

	List<LlmCall> findByPurpose(PromptPurpose purpose);
}
