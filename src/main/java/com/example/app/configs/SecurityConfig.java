package com.example.app.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors().disable()
				.csrf()
				.and()
				.authorizeHttpRequests()
				.requestMatchers("/", "/register", "/css/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.failureUrl("/login-error")
						.defaultSuccessUrl("/")
						.permitAll())
				.logout()
				.logoutSuccessUrl("/login");
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
