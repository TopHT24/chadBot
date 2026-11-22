package com.example.v1.dao;

import java.util.List;
import java.util.Optional;

/**
 * Basic persistence operations shared by every DAO.
 * The in-memory implementations in dao.inmemory can later be swapped for JDBC/JPA
 * implementations against Postgres without touching the service layer.
 */
public interface CrudDao<T, ID> {

	/** Inserts when the id is null (assigning one), otherwise updates. */
	T save(T entity);

	Optional<T> findById(ID id);

	List<T> findAll();

	boolean existsById(ID id);

	void deleteById(ID id);

	long count();
}
