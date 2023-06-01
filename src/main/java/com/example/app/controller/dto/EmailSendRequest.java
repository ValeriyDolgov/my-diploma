package com.example.app.controller.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailSendRequest {
    private String recipient;
}
