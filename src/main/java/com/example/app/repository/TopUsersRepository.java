package com.example.app.repository;

import com.example.app.service.dto.NumberOfArticlesByUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopUsersRepository extends JpaRepository<NumberOfArticlesByUser, Long> {
    @Query(value = "SELECT u.email, count(u.email) AS number_of_articles FROM users u, article a WHERE u.id=a.user_id GROUP BY u.email ORDER BY count(u.email) desc limit 5", nativeQuery = true)
    List<NumberOfArticlesByUser> getTopFiveUsersByArticles();
}
