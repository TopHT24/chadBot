package com.example.v1.model;

/**
 * Implemented by every persisted entity, so generic DAO code can read and assign ids.
 */
public interface Identifiable {

	Long getId();

	void setId(Long id);
}
