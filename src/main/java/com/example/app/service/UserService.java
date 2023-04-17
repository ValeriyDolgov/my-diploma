package com.example.app.service;

import com.example.app.model.Role;
import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import com.example.app.service.dto.UpdateUserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User getByLogin(String login) {
		return userRepository.findByLogin(login)
				.orElseThrow(() -> new UsernameNotFoundException(login));
	}

	public List<User> findAllUsers() {
		return userRepository.findAllByOrderByLogin();
	}

	public User registerNewUser(String login, String password) {
		var existingUser = userRepository.findByLogin(login);
		if (existingUser.isPresent()) {
			throw new IllegalArgumentException("Login is already registered: " + login);
		}
		var user = User.builder()
				.login(login)
				.password(passwordEncoder.encode(password))
				.roles(Set.of(Role.EMPLOYEE))
				.active(true)
				.build();
		return userRepository.save(user);
	}

	public boolean deactivateAccount(String login, String confirmation) {
		if (login.equals(confirmation)) {
			User user = getByLogin(login);
			user.setActive(false);
			return true;
		}
		return false;
	}

	public User updateUser(String login, UpdateUserRequest updateUserRequest) {
		var user = getByLogin(login);
		user.setEmail(updateUserRequest.getEmail());
		user.setPhoneNumber(updateUserRequest.getPhoneNumber());
		if (Strings.isNotBlank(updateUserRequest.getPassword())) {
			user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
		}
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return getByLogin(username);
	}
}
