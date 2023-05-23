package com.example.app.controller;

import com.example.app.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Properties;

@Controller
public class HomeController {

	@GetMapping("/")
	public String index(Authentication authentication) {
		if (authentication == null) {
			return "redirect:/login";
		} else if (authentication.getAuthorities().contains(Role.ADMIN)) {
			return "redirect:/admin";
		} else if (authentication.getAuthorities().contains(Role.MODERATOR)) {
		return "redirect:/moderator";
	}
		else {
			return "redirect:/user";
		}
	}

	@GetMapping("properties")
	@ResponseBody
	public Properties properties() {
		return System.getProperties();
	}
}
