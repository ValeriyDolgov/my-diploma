package com.example.app.service;

import com.example.app.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestorePasswordService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public void restorePasswordForUser(String newPassword, String confirmNewPassword, String token) {
        if (newPassword.equals(confirmNewPassword)) {
            User user = userService.getByResetPasswordToken(token);
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null);
            userService.saveUser(user);
        }
    }
}
