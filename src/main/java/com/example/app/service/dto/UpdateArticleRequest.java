package com.example.app.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateArticleRequest {
    private String title;
    private String description;
    private String text;
    private String collaborators;
    private String dateOfUpdate;
    private Boolean isModerated;
    private Boolean isPublished;
}
