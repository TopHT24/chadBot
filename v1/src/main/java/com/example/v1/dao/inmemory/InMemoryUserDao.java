package com.example.v1.dao.inmemory;

import com.example.v1.dao.UserDao;
import com.example.v1.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserDao extends InMemoryCrudDao<User> implements UserDao {

	@Override
	public Optional<User> findByEmail(String email) {
		return findFirstWhere(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email));
	}
}
