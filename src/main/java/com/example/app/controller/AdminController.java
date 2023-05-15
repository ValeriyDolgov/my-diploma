package com.example.app.controller;

import com.example.app.controller.dto.DeactivationData;
import com.example.app.model.User;
import com.example.app.service.UserService;
import com.example.app.service.dto.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping
    public String index() {
        return "admin/index";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/{email}")
    public String showUser(@PathVariable String email, Model model) {
        User user = userService.getByEmail(email);
//        Employee employee = user.getEmployee();
//        ContactInfo contactInfo = employee.getContactInfo();
        model.addAttribute("user", user);
//        model.addAttribute("employee", employee);
//        model.addAttribute("contactInfo", contactInfo);
        return "admin/user";
    }


    @GetMapping("/users/{email}/edit")
    public String showEditUserForm(@PathVariable String email, Model model) {
        var user = userService.getByEmail(email);
        user.setPassword(null);
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    @PostMapping("/users/{email}/edit")
    public String updatedUser(@PathVariable String email, @ModelAttribute User user) {
        userService.updateUser(email, UpdateUserRequest.builder()
                .surname(user.getSurname())
                .name(user.getName())
                .patronymic(user.getPatronymic())
                .birthday(user.getBirthday())
                .password(user.getPassword())
                .build());
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{email}/deactivate")
    public String deactivateUserProfile(@PathVariable String email, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("confirmationMismatched", false);
        model.addAttribute("deactivationData", new DeactivationData());
        return "admin/user-deactivation";
    }

    @PostMapping("/users/{email}/deactivate")
    public String deactivateAccountByUser(@PathVariable String email,
                                          @ModelAttribute DeactivationData deactivationData,
                                          Model model) {
        if (!userService.deactivateAccount(email, deactivationData.getConfirmation())) {
            model.addAttribute("confirmationMismatched", true);
            return "admin/user-deactivation";
        }
        return "redirect:/admin/users";
    }
}