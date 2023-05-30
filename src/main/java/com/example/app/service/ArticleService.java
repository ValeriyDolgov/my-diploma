package com.example.app.service;

import com.example.app.model.Article;
import com.example.app.model.User;
import com.example.app.repository.ArticleRepository;
import com.example.app.service.dto.ArticleRequest;
import com.example.app.service.dto.UpdateArticleRequest;
import com.example.app.service.mapper.MappingUtils;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MappingUtils mappingUtils;
    private final UserService userService;
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

    public Page<Article> findAllPageableArticles(Pageable pageable) {
        List<Article> articles = articleRepository.findAllByIsModeratedIsTrueAndIsPublishedIsTrue();
        return findPaginated(pageable, articles);
    }

    public Page<Article> findAllPageableArticlesByQuery(Pageable pageable, String query) {
        List<Article> articles = articleRepository.findByTitleOrTextContaining("%" + query + "%");
        return findPaginated(pageable, articles);
    }

    public Page<Article> findPaginated(Pageable pageable, List<Article> articles) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Article> list;

        if (articles.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, articles.size());
            list = articles.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), articles.size());
    }

    public Page<Article> showNotModeratedArticles(Pageable pageable) {
        List<Article> articles = articleRepository.findAllByIsModeratedIsFalseAndIsPublishedIsFalse();
        return findPaginated(pageable, articles);
    }

    public Page<Article> showModeratedAndNotPublishedArticles(Pageable pageable) {
        List<Article> articles = articleRepository.findAllByIsModeratedIsTrueAndIsPublishedIsFalse();
        return findPaginated(pageable, articles);
    }

    public Article getBySlug(String slug) {
        return articleRepository.getArticleBySlugContainingIgnoreCase(slug);
    }

    public void updateArticle(String slug, UpdateArticleRequest request) {
        var article = getBySlug(slug);
        article.setTitle(request.getTitle());
        article.setSlug(slugify.slugify(request.getTitle()));
        article.setDescription(request.getDescription());
        article.setText(request.getText());
        article.setCollaborators(request.getCollaborators());
        article.setDateOfUpdate(LocalDate.now());
        article.setIsModerated(request.getIsModerated());
        articleRepository.save(article);
    }

    public void publishArticle(String slug) {
        var article = getBySlug(slug);
        article.setIsPublished(true);
        articleRepository.save(article);
    }

    public void returnArticleToModerating(String slug) {
        var article = getBySlug(slug);
        article.setIsModerated(false);
        article.setIsPublished(false);
        articleRepository.save(article);
    }
}
