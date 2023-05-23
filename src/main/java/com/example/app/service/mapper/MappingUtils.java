package com.example.app.service.mapper;

import com.example.app.model.*;
import com.example.app.service.dto.ArticleRequest;
import com.example.app.service.dto.RegisterRequest;
import com.github.slugify.Slugify;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@Configuration
@AllArgsConstructor
public class MappingUtils {

    private final PasswordEncoder passwordEncoder;
    private final Slugify slugify;

    public User mapToUserFromRegisterRequest(RegisterRequest registerRequest) {
        return User.builder()
                .surname(registerRequest.getSurname())
                .name(registerRequest.getName())
                .patronymic(registerRequest.getPatronymic())
                .nickname(registerRequest.getNickname())
                .email(registerRequest.getEmail())
                .birthday(registerRequest.getBirthday())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .sex(String.valueOf(registerRequest.getGender()))
                .resetPasswordToken(null)
                .isCEO(false)
                .roles(Set.of(Role.EMPLOYEE))
                .active(true)
                .build();
    }

    public Employee mapToEmployeeFromRegisterRequest(RegisterRequest registerRequest) {
        return Employee.builder()
                .dateOfHire(null)
                .dateOfDismissal(null)
                .reasonForDismissal(null)
                .avatar(null)
                .dateOfCreation(LocalDateTime.now())
                .dateOfUpdate(LocalDateTime.now())
                .isOnRemote(null)
                .note(null)
                .contactInfo(null)
                .user(null)
                .build();
    }

    public ContactInfo mapToContactInfoFromRegisterRequest(RegisterRequest registerRequest) {
        return ContactInfo.builder()
                .employee(null)
                .officeNumber(null)
                .personalNumber(registerRequest.getPhoneNum())
                .officeEmail(registerRequest.getEmail())
                .personalEmail(null)
                .telegramAccount(registerRequest.getTelegram())
                .skypeAccount(registerRequest.getSkype())
                .build();
    }

    public Article mapToArticleFromArticleRequest(ArticleRequest request) {
        return Article.builder()
                .title(request.getTitle())
                .slug(slugify.slugify(request.getTitle()))
                .description(request.getDescription())
                .text(request.getText())
                .dateOfCreation(LocalDate.now())
                .author(null)
                .collaborators(request.getCollaborators())
                .isModerated(false)
                .isPublished(false)
                .build();
    }
}
