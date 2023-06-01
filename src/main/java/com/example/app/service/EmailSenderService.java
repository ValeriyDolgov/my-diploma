package com.example.app.service;

import com.example.app.model.User;
import com.example.app.service.utils.UrlPathUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final UserService userService;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public String sendSimpleMail(String recipient, HttpServletRequest request) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            User user = userService.getByEmail(recipient);
            if (user != null) {
                if (user.getResetPasswordToken() == null) {
                    user.setResetPasswordToken(RandomString.make(20));
                    String restorePasswordLink = UrlPathUtility.getSiteUrl(request);
                    mailMessage.setTo(recipient);
                    mailMessage.setSubject("Восстановление пароля на портале QDIS");
                    mailMessage.setText("Для восстановления пароля перейдите по данной ссылке: " +
                            restorePasswordLink + "/restorePassword/" + user.getResetPasswordToken());
                    userService.saveUser(user);
                    javaMailSender.send(mailMessage);
                    return "Mail send successfully";
                } else return "Current request can't be executed because exist old ResetPassword Token";
            } else return "Current mail doesn't exist in DataBase";

        } catch (Exception e) {
            return "Error while sending email";
        }
    }
}
