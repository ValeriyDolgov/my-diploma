package com.example.app.service.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class NumberOfArticlesByUser {
    private String email;
    @Id
    private Long numberOfArticles;
}
