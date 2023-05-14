package com.example.app.service.dto;

import com.example.app.model.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    @NotEmpty(message = "Укажите фамилию")
    private String surname;
    @NotEmpty(message = "Укажите имя")
    private String name;
    private String patronymic;
    @NotEmpty
    private String nickname;
    @NotNull
    private LocalDate birthday;
    @NotEmpty(message = "Укажите корпоративную почту")
    @Length(max = 256)
    @Email(regexp = "[a-zA-Z0-9._%+-]+@qazdevelop.com", message = "Ваша почта почта содержит некорректный символ или " +
            "домен почты не @qazdevelop.com")
    private String email;
    private Gender gender;
    @NotEmpty(message = "Укажите номер телефона")
    //Phone number validation should be on frontend cuz it isn't used in logic
    private String phoneNum;
    @NotEmpty(message = "Укажите пароль")
    private String password;
    @NotEmpty(message = "Укажите имя Telegram-аккаунта")
    private String telegram;
    @NotEmpty(message = "Укажите имя Skype-аккаунта")
    private String skype;
    @NotEmpty(message = "Укажите страну текущего проживания")
    private String country;
    @NotEmpty(message = "Укажите город текущего проживания")
    private String city;
}
