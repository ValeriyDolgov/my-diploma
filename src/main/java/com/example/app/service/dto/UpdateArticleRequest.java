package com.example.app.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UpdateArticleRequest {
    private String title;
    private String description;
    private String text;
    private String collaborators;
    private String dateOfUpdate;
    private Boolean isModerated;
}
