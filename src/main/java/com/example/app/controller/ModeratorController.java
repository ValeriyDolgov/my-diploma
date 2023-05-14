package com.example.app.controller;

import com.example.app.model.Article;
import com.example.app.service.ArticleService;
import com.example.app.service.dto.UpdateArticleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/moderator")
@PreAuthorize(value = "hasAuthority('MODERATOR')")
public class ModeratorController {
    private final ArticleService articleService;

    @GetMapping
    public String mainPage() {
        return "moderator/index";
    }

    @GetMapping("/notModerated")
    public String showNotModeratedArticles(Model model) {
        model.addAttribute("listOfArticles", articleService.showNotModeratedArticles());
        model.addAttribute("numberOfArticles", articleService.showNotModeratedArticles().size());
        return "moderator/not-moderated-list";
    }

    @GetMapping("/notModerated/{slug}/edit")
    public String showCheckArticleForm(@PathVariable String slug, Model model) {
        var article = articleService.getBySlug(slug);
        model.addAttribute("article", article);
        return "moderator/edit-article";
    }

    @PostMapping("/notModerated/{slug}/edit")
    public String moderateArticle(@PathVariable String slug, @ModelAttribute Article article) {
        articleService.updateArticle(slug, UpdateArticleRequest.builder()
                .title(article.getTitle())
                .description(article.getDescription())
                .text(article.getText())
                .collaborators(article.getCollaborators())
                .isModerated(true)
                .build());
        return "redirect:/moderator/notModerated";
    }
}