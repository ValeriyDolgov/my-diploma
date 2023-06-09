package com.example.app.service.mapper;

import com.example.app.model.*;
import com.example.app.service.dto.ArticleRequest;
import com.example.app.service.dto.RegisterRequest;
import com.example.app.service.dto.UpdateUserRequest;
import com.github.slugify.Slugify;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
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
                .roles(Set.of(Role.EMPLOYEE))
                .active(true)
                .country(registerRequest.getCountry())
                .city(registerRequest.getCity())
                .departments(null)
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
                .position(Position.None)
                .build();
    }

    public User mapUserFromUpdateUserRequest(User user, UpdateUserRequest updateUserRequest) {
        user.setSurname(updateUserRequest.getSurname());
        user.setName(updateUserRequest.getName());
        user.setPatronymic(updateUserRequest.getPatronymic());
        if (updateUserRequest.getBirthday() != null) {
            user.setBirthday(updateUserRequest.getBirthday());
        }
        if (Strings.isNotBlank(updateUserRequest.getPassword())) {
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }
        user.getEmployee().setDateOfUpdate(LocalDateTime.now());
        user.setSex(String.valueOf(updateUserRequest.getGender()));
        user.setCountry(updateUserRequest.getCountry());
        user.setCity(updateUserRequest.getCity());
        user.getEmployee().getContactInfo().setPersonalNumber(updateUserRequest.getPhoneNumber());
        user.getEmployee().getContactInfo().setTelegramAccount(updateUserRequest.getTelegramAccount());
        user.getEmployee().getContactInfo().setSkypeAccount(updateUserRequest.getSkypeAccount());
        user.setCountry(updateUserRequest.getCountry());
        user.setCity(updateUserRequest.getCity());
        return user;
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
