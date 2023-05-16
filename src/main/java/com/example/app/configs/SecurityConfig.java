package com.example.app.configs;

import com.github.slugify.Slugify;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/", "/register", "/css/**", "/user/assets/**", "/user/webjars/**", "/assets/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureUrl("/login-error")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .rememberMe()
                .tokenValiditySeconds(5 * 24 * 60 * 60)
                .key("AbcdefghiJklmNoPqRstUvXyz")
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID", "remember-me");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public Slugify slugify() {
        return Slugify.builder().build();
    }
}
