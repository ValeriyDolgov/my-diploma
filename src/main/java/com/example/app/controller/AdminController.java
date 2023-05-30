package com.example.app.controller;

import com.example.app.controller.dto.DeactivationData;
import com.example.app.controller.dto.DepartmentNewWorkerDto;
import com.example.app.model.Department;
import com.example.app.model.Position;
import com.example.app.model.Role;
import com.example.app.model.User;
import com.example.app.service.DepartmentService;
import com.example.app.service.UserService;
import com.example.app.service.dto.DepartmentCreateRequest;
import com.example.app.service.dto.UpdateUserByAdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public String showUsers(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<User> userPage = userService.findAllPageableUsers(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("userPage", userPage);
        model.addAttribute("pageNumbers", userService.getListOfPageNumbersForPagination(userPage));

        return "admin/users";
    }

    @GetMapping("/departments")
    public String showDepartments(Model model) {
        model.addAttribute("listOfDepartments", departmentService.getListOfAllDepartments());
        return "admin/departments/departments";
    }

    @GetMapping("/departments/new")
    public String showDepartmentCreateForm(Model model) {
        DepartmentCreateRequest request = new DepartmentCreateRequest();
        model.addAttribute("request", request);
        return "admin/departments/new-department";
    }

    @PostMapping("/departments/new")
    public String saveNewDepartment(@ModelAttribute DepartmentCreateRequest request) {
        departmentService.saveNewDepartment(request);
        return "redirect:/admin/departments";
    }

    @GetMapping("/departments/{departmentName}")
    public String showDepartmentDetails(@PathVariable String departmentName, Model model) {
        Department department = departmentService.getDepartmentByName(departmentName);
        List<User> listOfWorker = department.getWorkers();
        model.addAttribute("department", department);
        model.addAttribute("listOfWorkers", listOfWorker);
        model.addAttribute("numberOfWorkers", listOfWorker.size());
        return "admin/departments/department-by-name";
    }

    @GetMapping("/departments/{departmentName}/newWorker")
    public String showFormForAddingNewWorkerToDepartment(@PathVariable String departmentName, Model model) {
        DepartmentNewWorkerDto dto = new DepartmentNewWorkerDto();
        model.addAttribute("departmentName", departmentName);
        model.addAttribute("departmentNewWorker", dto);
        return "admin/departments/new-worker-for-department";
    }

    @PostMapping("/departments/{departmentName}/newWorker")
    public String saveNewWorkerToDepartment(@PathVariable String departmentName, @ModelAttribute DepartmentNewWorkerDto dto) {
        departmentService.saveNewEmployeeToDepartment(departmentName, dto.getEmailOfWorker());
        return "redirect:/admin/departments/{departmentName}";
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
        model.addAttribute("roles", Role.values());
        model.addAttribute("positions", Position.values());
        model.addAttribute("departments", departmentService.getListOfAllDepartments());
        return "admin/edit-user";
    }

    @PostMapping("/users/{email}/edit")
    public String updatedUser(@PathVariable String email, @ModelAttribute User user) {
        userService.updateUserByAdmin(email, UpdateUserByAdminRequest.builder()
                .email(user.getEmail())
                .roles(user.getRoles())
                .password(user.getPassword())
                .position(user.getEmployee().getPosition())
                .department(user.getDepartments())
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