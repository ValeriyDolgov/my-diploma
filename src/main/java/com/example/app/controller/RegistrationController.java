package com.example.app.controller;

import com.example.app.controller.dto.RegistrationData;
import com.example.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

	private final UserService userService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "login";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		RegistrationData registrationData = new RegistrationData();
		model.addAttribute("registrationData", registrationData);
		return "registration";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute RegistrationData dto) {
		userService.registerNewUser(dto.getLogin(), dto.getPassword());
		return "redirect:/login";
	}
}
