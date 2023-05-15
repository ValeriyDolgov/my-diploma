package com.example.app.service;

import com.example.app.model.Article;
import com.example.app.model.User;
import com.example.app.repository.ArticleRepository;
import com.example.app.service.dto.ArticleRequest;
import com.example.app.service.dto.UpdateArticleRequest;
import com.example.app.service.mapper.MappingUtils;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final MappingUtils mappingUtils;
    private final Slugify slugify;

    public void saveNewArticle(String userEmail, ArticleRequest articleRequest) {
        if (articleRepository.getArticleByTitle(articleRequest.getTitle()) == null) {
            Article article = mappingUtils.mapToArticleFromArticleRequest(articleRequest);
            article.setText(article.getText().replaceAll("\r\n", "<br>"));
            User user = userService.getByEmail(userEmail);
            article.setAuthor(user);
            articleRepository.save(article);
        }
    }

    public List<Article> showPublishedArticles() {
        return articleRepository.findAllByIsModeratedIsTrueAndIsPublishedIsTrue();
    }

    public List<Article> showModeratedArticlesByQuery(String query) {
        return articleRepository.findByTitleOrTextContaining("%" + query + "%");
    }

    public List<Article> showNotModeratedArticles() {
        return articleRepository.findAllByIsModeratedIsFalseAndIsPublishedIsFalse();
    }

    public List<Article> showModeratedAndNotPublishedArticles() {
        return articleRepository.findAllByIsModeratedIsTrueAndIsPublishedIsFalse();
    }

    public Article getBySlug(String slug) {
        return articleRepository.getArticleBySlugContainingIgnoreCase(slug);
    }

    public Article updateArticle(String slug, UpdateArticleRequest request) {
        var article = getBySlug(slug);
        article.setTitle(request.getTitle());
        article.setSlug(slugify.slugify(request.getTitle()));
        article.setDescription(request.getDescription());
        article.setText(request.getText());
        article.setCollaborators(request.getCollaborators());
        article.setDateOfUpdate(LocalDate.now());
        article.setIsModerated(request.getIsModerated());
        articleRepository.save(article);
        return article;
    }

    public Article publishArticle(String slug) {
        var article = getBySlug(slug);
        article.setIsPublished(true);
        articleRepository.save(article);
        return article;
    }

    public Article returnArticleToModerating(String slug) {
        var article = getBySlug(slug);
        article.setIsModerated(false);
        article.setIsPublished(false);
        articleRepository.save(article);
        return article;
    }
}
