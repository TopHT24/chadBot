package com.example.v1.dao.inmemory;

import com.example.v1.dao.CrudDao;
import com.example.v1.model.Identifiable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

/**
 * Thread-safe in-memory storage used until Postgres is wired in.
 * Ids are generated like a database sequence. Data is lost on restart.
 *
 * Note: stored objects are returned by reference, so mutating a returned entity
 * changes the stored one even without calling save(). Always call save() anyway,
 * so the code keeps working once a real database backs the DAO.
 */
public abstract class InMemoryCrudDao<T extends Identifiable> implements CrudDao<T, Long> {

	private final Map<Long, T> store = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong();

	@Override
	public T save(T entity) {
		if (entity.getId() == null) {
			entity.setId(sequence.incrementAndGet());
		} else {
			// keep the sequence ahead of manually assigned ids
			sequence.accumulateAndGet(entity.getId(), Math::max);
		}
		store.put(entity.getId(), entity);
		return entity;
	}

	@Override
	public Optional<T> findById(Long id) {
		return id == null ? Optional.empty() : Optional.ofNullable(store.get(id));
	}

	@Override
	public List<T> findAll() {
		return findWhere(e -> true);
	}

	@Override
	public boolean existsById(Long id) {
		return id != null && store.containsKey(id);
	}

	@Override
	public void deleteById(Long id) {
		if (id != null) {
			store.remove(id);
		}
	}

	@Override
	public long count() {
		return store.size();
	}

	/** All entities matching the filter, ordered by id (i.e. insertion order). */
	protected List<T> findWhere(Predicate<T> filter) {
		return store.values().stream()
				.filter(filter)
				.sorted(Comparator.comparing(Identifiable::getId))
				.toList();
	}

	protected Optional<T> findFirstWhere(Predicate<T> filter) {
		return findWhere(filter).stream().findFirst();
	}
}
