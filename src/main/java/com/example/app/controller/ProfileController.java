package com.example.app.controller;


import com.example.app.model.User;
import com.example.app.service.UserService;
import com.example.app.service.dto.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
@PreAuthorize("hasAuthority('EMPLOYEE')")
public class ProfileController {

	private final UserService userService;

	@GetMapping
	public String showProfile(Authentication authentication, Model model) {
		var user = userService.getByLogin(authentication.getName());
		model.addAttribute("user", user);
		return "profile";
	}

	@GetMapping("/edit")
	public String showEditProfileForm(Authentication authentication, Model model) {
		var user = userService.getByLogin(authentication.getName());
		user.setPassword(null);
		model.addAttribute("user", user);
		return "edit-profile";
	}

	@PostMapping("/edit")
	public String updateProfile(Authentication authentication, @ModelAttribute User user) {
		userService.updateUser(authentication.getName(), UpdateUserRequest.builder()
				.email(user.getEmail())
				.phoneNumber(user.getPhoneNumber())
				.password(user.getPassword())
				.build());
		return "redirect:/profile";
	}
}
