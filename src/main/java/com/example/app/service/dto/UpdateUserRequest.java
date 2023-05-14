package com.example.app.service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateUserRequest {
	private String surname;
	private String name;
	private String patronymic;
	private LocalDate birthday;
	private String password;
}
