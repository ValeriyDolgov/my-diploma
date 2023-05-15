package com.example.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "article")
public class Article {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "slug")
    private String slug;
    @Column(name = "description")
    private String description;
    @Column(name = "text")
    private String text;
    @Column(name = "date_of_creation")
    private LocalDate dateOfCreation;
    @Column(name = "date_of_update")
    private LocalDate dateOfUpdate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private User author;
    @Column(name = "collaborators")
    private String collaborators;
    @Column(name = "is_moderated")
    private Boolean isModerated;
    @Column(name = "is_published")
    private Boolean isPublished;
}
