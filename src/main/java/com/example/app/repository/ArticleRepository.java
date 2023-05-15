package com.example.app.repository;

import com.example.app.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value = "SELECT * FROM article WHERE title LIKE :query OR text LIKE :query", nativeQuery = true)
    List<Article> findByTitleOrTextContaining(@Param("query") String query);

//    List<Article> findAllByTitleContainsIgnoreCaseOrTextContainingIgnoreCase(String query);
    List<Article> findAllByIsModeratedIsFalseAndIsPublishedIsFalse();
    List<Article> findAllByIsModeratedIsTrueAndIsPublishedIsFalse();
    List<Article> findAllByIsModeratedIsTrueAndIsPublishedIsTrue();
    Article getArticleByTitle(String title);
    Article getArticleBySlugContainingIgnoreCase(String slug);
}
