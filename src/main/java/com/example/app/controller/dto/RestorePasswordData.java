package com.example.app.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestorePasswordData {
    private String newPassword;
    private String confirmationNewPassword;
}
