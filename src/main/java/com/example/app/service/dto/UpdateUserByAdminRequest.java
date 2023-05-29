package com.example.app.service.dto;

import com.example.app.model.Position;
import com.example.app.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
@Builder
public class UpdateUserByAdminRequest {
    @NotEmpty(message = "Укажите корпоративную почту")
    @Length(max = 256)
    @Email(regexp = "[a-zA-Z0-9._%+-]+@qazdevelop.com", message = "Ваша почта почта содержит некорректный символ или " +
            "домен почты не @qazdevelop.com")
    private String email;
    private String password;
    private Set<Role> roles;
    private Position position;
    private String nameOfDepartment;
}
