package com.example.app.controller;


import com.example.app.model.Article;
import com.example.app.model.Employee;
import com.example.app.model.Gender;
import com.example.app.model.User;
import com.example.app.service.UserService;
import com.example.app.service.dto.UpdateUserRequest;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@PreAuthorize("hasAuthority('EMPLOYEE')")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String mainPage() {
        return "user/index";
    }

    @GetMapping("/all")
    public String showUsersPageable(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<User> userPage = userService.findAllPageableUsers(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("userPage", userPage);
        model.addAttribute("pageNumbers", userService.getListOfPageNumbersForPagination(userPage));

        return "user/pageable-list";
    }

    @GetMapping("/all/search")
    public String showUsersByQuery(@RequestParam("page") Optional<Integer> page,
                                   @RequestParam("size") Optional<Integer> size,
                                   @RequestParam("query") String query,
                                   Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<User> userPage = userService.findAllPageableUsersByQuery(PageRequest.of(currentPage - 1, pageSize), query);
        model.addAttribute("query", query);
        model.addAttribute("userPage", userPage);
        model.addAttribute("pageNumbers", userService.getListOfPageNumbersForPagination(userPage));
        return "user/pageable-list";
    }

    @GetMapping("/{email}")
    public String showUser(@PathVariable String email, Model model) {
        User user = userService.getByEmail(email);
        Employee employee = user.getEmployee();
        List<Article> listOfArticle = userService.getListOfAllArticlesForUser(email);
        model.addAttribute("user", user);
        model.addAttribute("employee", employee);
        model.addAttribute("contactInfo", employee.getContactInfo());
        model.addAttribute("listOfArticles", listOfArticle);
        model.addAttribute("numberOfArticles", listOfArticle.size());
        return "user/user";
    }

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        var user = userService.getByEmail(authentication.getName());
        List<Article> articleList = userService.getListOfAllArticlesForUser(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("listOfArticles", articleList);
        model.addAttribute("numberOfArticles", articleList.size());
        return "user/profile";
    }

    @GetMapping("/profile/edit")
    public String showEditProfileForm(Authentication authentication, Model model) {
        var user = userService.getByEmail(authentication.getName());
        user.setBirthday(null);
        user.setPassword(null);
        model.addAttribute("user", user);
        return "user/edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(Authentication authentication, @ModelAttribute User user) {
        userService.updateUser(authentication.getName(), UpdateUserRequest.builder()
                .surname(user.getSurname())
                .name(user.getName())
                .patronymic(user.getPatronymic())
                .birthday(user.getBirthday())
                .gender(Gender.valueOf(user.getSex()))
                .country(user.getCountry())
                .city(user.getCity())
                .phoneNumber(user.getEmployee().getContactInfo().getPersonalNumber())
                .telegramAccount(user.getEmployee().getContactInfo().getTelegramAccount())
                .skypeAccount(user.getEmployee().getContactInfo().getSkypeAccount())
                .password(user.getPassword())
                .build());
        return "redirect:/user/profile";
    }
}