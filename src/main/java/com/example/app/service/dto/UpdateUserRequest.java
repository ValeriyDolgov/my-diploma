package com.example.app.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {
    private String surname;
    private String name;
    private String patronymic;
    private String password;
}
