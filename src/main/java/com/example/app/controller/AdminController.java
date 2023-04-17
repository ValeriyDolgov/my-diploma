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

	@GetMapping("/users/{login}")
	public String showUser(@PathVariable String login, Model model) {
		model.addAttribute("user", userService.getByLogin(login));
		return "admin/user";
	}


	@GetMapping("/users/{login}/edit")
	public String showEditUserForm(@PathVariable String login, Model model) {
		var user = userService.getByLogin(login);
		user.setPassword(null);
		model.addAttribute("user", user);
		return "admin/edit-user";
	}

	@PostMapping("/users/{login}/edit")
	public String updatedUser(@PathVariable String login, @ModelAttribute User user) {
		userService.updateUser(login, UpdateUserRequest.builder()
				.email(user.getEmail())
				.phoneNumber(user.getPhoneNumber())
				.password(user.getPassword())
				.build());
		return "redirect:/admin/users";
	}

	@GetMapping("/users/{login}/deactivate")
	public String deactivateUserProfile(@PathVariable String login, Model model) {
		model.addAttribute("login", login);
		model.addAttribute("confirmationMismatched", false);
		model.addAttribute("deactivationData", new DeactivationData());
		return "admin/user-deactivation";
	}

	@PostMapping("/users/{login}/deactivate")
	public String deactivateAccountByUser(@PathVariable String login,
	                                      @ModelAttribute DeactivationData deactivationData,
	                                      Model model) {
		if (!userService.deactivateAccount(login, deactivationData.getConfirmation())) {
			model.addAttribute("confirmationMismatched", true);
			return "admin/user-deactivation";
		}
		return "redirect:/admin/users";
	}
}