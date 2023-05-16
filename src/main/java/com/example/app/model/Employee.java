package com.example.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_of_hire")
    private LocalDate dateOfHire;
    @Column(name = "date_of_dismissal")
    private LocalDate dateOfDismissal;
    @Column(name = "reason_for_dismissal")
    private String reasonForDismissal;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;
    @Column(name = "date_of_update")
    private LocalDateTime dateOfUpdate;
    @Column(name = "is_on_remote")
    private Boolean isOnRemote;
    @Column(name = "note")
    private String note;
    @Column(name = "timezone")
    private String timezone;
    @OneToOne(fetch = FetchType.EAGER)
    private ContactInfo contactInfo;
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;
}