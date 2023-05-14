package com.example.app.controller;

import com.example.app.model.Article;
import com.example.app.service.ArticleService;
import com.example.app.service.dto.ArticleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
@PreAuthorize("hasAuthority('EMPLOYEE')")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/new")
    public String showArticleForm(Model model) {
        ArticleRequest articleRequest = new ArticleRequest();
        model.addAttribute("article", articleRequest);
        return "article/new-form";
    }

    @PostMapping("/new")
    public String saveNewArticle(@ModelAttribute ArticleRequest articleRequest, Authentication authentication) {
        articleService.saveNewArticle(authentication.getName(), articleRequest);
        return "redirect:/user";
    }

    @GetMapping("/all")
    public String showOnlyModeratedArticles(Model model) {
        model.addAttribute("listOfArticles", articleService.showModeratedArticles());
        return "article/moderated-list";
    }

    @GetMapping("/all/search")
    public String showOnlyModeratedArticlesByQuery(@RequestParam("query") String query, Model model) {
        List<Article> articleList = articleService.showModeratedArticlesByQuery(query);
        model.addAttribute("numberOfResults", articleList.size());
        model.addAttribute("listOfArticles", articleList);
        return "article/moderated-list-by-query";
    }

    @GetMapping("/{slug}")
    public String showModeratedArticle(@PathVariable String slug, Model model) {
        var article = articleService.getBySlug(slug);
        model.addAttribute("article", article);
        return "article/article";
    }
}