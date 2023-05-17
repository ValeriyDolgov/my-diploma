package com.example.app.controller;

import com.example.app.repository.TopUsersRepository;
import com.example.app.service.dto.NumberOfArticlesByUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TopOfUsersController {
    private final TopUsersRepository topUsersRepository;
    @GetMapping("/topFive")
    public String getTopFive(Model model) {
        List<NumberOfArticlesByUser> topFive = topUsersRepository.getTopFiveUsersByArticles();
        model.addAttribute("list", topFive);
        return "top";
    }
}
