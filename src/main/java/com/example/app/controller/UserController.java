package com.example.app.controller;


import com.example.app.model.Employee;
import com.example.app.model.User;
import com.example.app.service.UserService;
import com.example.app.service.dto.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String showUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "user/users";
    }

    @GetMapping("/{email}")
    public String showUser(@PathVariable String email, Model model) {
        User user = userService.getByEmail(email);
        Employee employee = user.getEmployee();
        model.addAttribute("user", user);
        model.addAttribute("employee", employee);
        model.addAttribute("contactInfo", employee.getContactInfo());
        return "user/user";
    }

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        var user = userService.getByEmail(authentication.getName());
        model.addAttribute("user", user);
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
                .password(user.getPassword())
                .build());
        return "redirect:/user/profile";
    }
}
