package com.example.app.controller;

import com.example.app.controller.dto.RestorePasswordData;
import com.example.app.service.RestorePasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restorePassword")
public class RestorePasswordController {
    private final RestorePasswordService restorePasswordService;

    @GetMapping("/{token}")
    public String showFormForRestorePassword(@PathVariable String token, Model model) {
        RestorePasswordData data = new RestorePasswordData();
        model.addAttribute("restorePasswordData", data);
        model.addAttribute("restorePasswordToken", token);
        return "restore-password-form";
    }

    @PostMapping("/{token}")
    public String resetPasswordForUser(@PathVariable String token, @ModelAttribute RestorePasswordData data) {
        restorePasswordService.restorePasswordForUser(data.getNewPassword(), data.getConfirmationNewPassword(), token);
        return "redirect:/login";
    }
}
