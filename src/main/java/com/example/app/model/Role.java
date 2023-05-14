package com.example.app.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    MODERATOR,
    EMPLOYEE;

    @Override
    public String getAuthority() {
        return this.name();
    }
}