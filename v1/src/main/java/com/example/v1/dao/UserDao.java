package com.example.v1.dao;

import com.example.v1.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends CrudDao<User, Long> {

	Optional<User> findByEmail(String email);
}
