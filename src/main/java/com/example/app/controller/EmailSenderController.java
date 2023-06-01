package com.example.app.controller;

import com.example.app.controller.dto.EmailSendRequest;
import com.example.app.service.EmailSenderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class EmailSenderController {
    private final EmailSenderService service;

    @GetMapping("/sendEmail")
    public String showFormForUserEmailForSendingEmail(Model model) {
        EmailSendRequest emailSendRequest = new EmailSendRequest();
        model.addAttribute("emailSendRequest", emailSendRequest);
        return "send-email-form";
    }

    @PostMapping("/sendEmail")
    public String sendRestorePasswordEmail(@ModelAttribute EmailSendRequest emailSendRequest, HttpServletRequest httpServletRequest) {
        service.sendSimpleMail(emailSendRequest.getRecipient(), httpServletRequest);
        return "redirect:/login";
    }
}
