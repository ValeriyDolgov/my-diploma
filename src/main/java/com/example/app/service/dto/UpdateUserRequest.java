package com.example.app.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {
	private String email;
	private String password;
	private String phoneNumber;
}
