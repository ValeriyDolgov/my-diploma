package com.example.app.controller;

import com.example.app.model.Article;
import com.example.app.service.ArticleService;
import com.example.app.service.dto.ArticleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
    public String showPublishedArticles(Model model) {
        model.addAttribute("listOfArticles", articleService.showPublishedArticles());
        return "article/moderated-list";
    }

    @GetMapping("/all/pageable")
    public String showArticlesPageable(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Article> articlePage = articleService.findAllPageableArticles(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("articlePage", articlePage);

        int totalPages = articlePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "article/pageable-moderated";
    }

    @GetMapping("/all/search")
    public String showOnlyModeratedArticlesByQuery(@RequestParam("page") Optional<Integer> page,
                                                   @RequestParam("size") Optional<Integer> size,
                                                   @RequestParam("query") String query,
                                                   Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Article> articlePage = articleService.findAllPageableArticlesByQuery(PageRequest.of(currentPage - 1, pageSize), query);
        model.addAttribute("query", query);
        model.addAttribute("articlePage", articlePage);

        int totalPages = articlePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "article/moderated-list-by-query";
    }

    @GetMapping("/{slug}")
    public String showModeratedArticle(@PathVariable String slug, Model model) {
        var article = articleService.getBySlug(slug);
        model.addAttribute("article", article);
        return "article/article";
    }
}