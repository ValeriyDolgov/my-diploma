package com.example.app.repository;

import com.example.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByLogin(String login);

	List<User> findAllByOrderByLogin();
}
