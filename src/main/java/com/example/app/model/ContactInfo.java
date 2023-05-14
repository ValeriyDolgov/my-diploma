package com.example.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contact_info")
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @OneToOne(mappedBy = "contactInfo", cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Employee employee;
    @Column(name = "office_number")
    private String officeNumber;
    @Column(name = "personal_number")
    private String personalNumber;
    @Column(name = "office_email")
    private String officeEmail;
    @Column(name = "personal_email")
    private String personalEmail;
    @Column(name = "telegram_acc")
    private String telegramAccount;
    @Column(name = "skype_acc")
    private String skypeAccount;
}
