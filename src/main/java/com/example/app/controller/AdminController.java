package com.example.app.controller;

import com.example.app.controller.dto.DeactivationData;
import com.example.app.model.Position;
import com.example.app.model.User;
import com.example.app.service.DepartmentService;
import com.example.app.service.UserService;
import com.example.app.service.dto.DepartmentCreateRequest;
import com.example.app.service.dto.UpdateUserByAdminRequest;
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
    private final DepartmentService departmentService;

    @GetMapping
    public String index() {
        return "admin/index";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    @GetMapping("/departments")
    public String showDepartments(Model model) {
        model.addAttribute("listOfDepartments", departmentService.getListOfAllDepartments());
        return "admin/departments";
    }

    @GetMapping("/departments/new")
    public String showDepartmentCreateForm(Model model) {
        DepartmentCreateRequest request = new DepartmentCreateRequest();
        model.addAttribute("request", request);
        return "admin/new-department";
    }

    @PostMapping("/departments/new")
    public String saveNewDepartment(@ModelAttribute DepartmentCreateRequest request) {
        departmentService.saveNewDepartment(request);
        return "redirect:/departments";
    }

    @GetMapping("/users/{email}")
    public String showUser(@PathVariable String email, Model model) {
        User user = userService.getByEmail(email);
        model.addAttribute("user", user);
        return "admin/user";
    }


    @GetMapping("/users/{email}/edit")
    public String showEditUserForm(@PathVariable String email, Model model) {
        var user = userService.getByEmail(email);
        user.setEmail(null);
        user.setPassword(null);
        model.addAttribute("user", user);
        model.addAttribute("email", email);
        model.addAttribute("departments", user.getDepartments());
        model.addAttribute("positions", Position.values());
        return "admin/edit-user";
    }

    @PostMapping("/users/{email}/edit")
    public String updatedUser(@PathVariable String email, @ModelAttribute User user) {
        userService.updateUserByAdmin(email, UpdateUserByAdminRequest.builder()
                .email(user.getEmail())
                .roles(user.getRoles())
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

    @PostMapping("/users/{email}/activate")
    public String activateAccountByEmail(@PathVariable String email) {
        userService.activateUserAccount(email);
        return "redirect:/admin/users";
    }
}