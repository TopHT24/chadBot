package com.example.v1.dao;

import com.example.v1.model.SourceMaterial;

import java.util.List;

public interface SourceMaterialDao extends CrudDao<SourceMaterial, Long> {

	List<SourceMaterial> findByUserId(Long userId);
}
