package com.example.app.repository;

import com.example.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE users.surname LIKE :query OR users.name LIKE :query OR users.patronymic LIKE :query OR users.email LIKE :query", nativeQuery = true)
    List<User> findBySurnameOrNameOrPatronymicOrEmailContaining(@Param("query") String query);

    Optional<User> findByEmail(String email);

    User findUserByIsCEOIsTrue();

    List<User> findAllByOrderById();
}
