package com.example.app.controller;

import com.example.app.model.Article;
import com.example.app.service.ArticleService;
import com.example.app.service.dto.UpdateArticleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
    public String showNotModeratedArticles(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Article> articlePage = articleService.showNotModeratedArticles(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("articlePage", articlePage);

        int totalPages = articlePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }
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
        return "redirect:/moderator/moderated/{slug}/publish";
    }

    @GetMapping("/moderated")
    public String showModeratedArticles(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Article> articlePage = articleService.showModeratedAndNotPublishedArticles(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("articlePage", articlePage);

        int totalPages = articlePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "moderator/moderated-list";
    }

    @GetMapping("/moderated/{slug}/publish")
    public String checkArticleBeforePublish(@PathVariable String slug, Model model) {
        model.addAttribute("article", articleService.getBySlug(slug));
        return "moderator/check-article";
    }

    @PostMapping("/moderated/{slug}/publish")
    public String publishArticle(@PathVariable String slug) {
        articleService.publishArticle(slug);
        return "redirect:/moderator/notModerated";
    }

    @PostMapping("/moderated/{slug}/return")
    public String returnArticleToEditing(@PathVariable String slug) {
        articleService.returnArticleToModerating(slug);
        return "redirect:/moderator/notModerated/{slug}/edit";
    }
}