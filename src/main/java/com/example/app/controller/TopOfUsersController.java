package com.example.app.controller;

import com.example.app.repository.TopUsersRepository;
import com.example.app.service.dto.NumberOfArticlesByUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequiredArgsConstructor
public class TopOfUsersController {
    private final TopUsersRepository topUsersRepository;
    @GetMapping("/topFive")
    public String getTopFive(Model model) {
        List<NumberOfArticlesByUser> topFive = topUsersRepository.getTopFiveUsersByArticles();
        Map<String, Integer> chartDate = new TreeMap<>();
        for (NumberOfArticlesByUser user : topFive) {
            chartDate.put(user.getEmail(), user.getNumberOfArticles());
        }
        model.addAttribute("chartData", chartDate);
        return "top";
    }
}
