package com.example.app.service;

import com.example.app.model.Department;
import com.example.app.model.User;
import com.example.app.repository.ContactInfoRepository;
import com.example.app.repository.DepartmentRepository;
import com.example.app.repository.EmployeeRepository;
import com.example.app.repository.UserRepository;
import com.example.app.service.dto.Node;
import com.example.app.service.dto.RegisterRequest;
import com.example.app.service.dto.UpdateUserRequest;
import com.example.app.service.mapper.MappingUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final ContactInfoRepository contactInfoRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MappingUtils mappingUtils;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public List<User> findAllUsers() {
        return userRepository.findAllByOrderByEmail();
    }

    public void registerNewUser(RegisterRequest registerRequest) {
        var existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email is already registered: " + registerRequest.getEmail());
        }
        var user = mappingUtils.mapToUserFromRegisterRequest(registerRequest);
        var employee = mappingUtils.mapToEmployeeFromRegisterRequest(registerRequest);
        var contactInfo = mappingUtils.mapToContactInfoFromRegisterRequest(registerRequest);
        employee.setContactInfo(contactInfo);
        contactInfo.setEmployee(employee);
        user.setEmployee(employee);
        contactInfoRepository.save(contactInfo);
        employeeRepository.save(employee);
        userRepository.save(user);
    }

    public boolean deactivateAccount(String login, String confirmation) {
        if (login.equals(confirmation)) {
            User user = getByEmail(login);
            user.setActive(false);
            return true;
        }
        return false;
    }

    public User updateUser(String login, UpdateUserRequest updateUserRequest) {
        var user = getByEmail(login);
        user.setSurname(updateUserRequest.getSurname());
        user.setName(updateUserRequest.getName());
        user.setPatronymic(updateUserRequest.getPatronymic());
        if (Strings.isNotBlank(updateUserRequest.getPassword())) {
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }
        return user;
    }

    public List<Node> getListOfNodesForWorkerTree() {
        List<Node> nodes = new ArrayList<>();
        User ceo = userRepository.findUserByIsCEOIsTrue();
        nodes.add(new Node("CEO", "0", "CEO", ceo.getEmail()));
        List<Department> departmentList = departmentRepository.findAll();
        for (Department department : departmentList) {
            nodes.add(new Node(department.getName(), "CEO", department.getName(),
                    department.getHeadOfDepartment().getEmail()));
            List<User> workers = department.getWorkers();
            for (User worker : workers) {
                String position = String.valueOf(worker.getEmployee().getPosition());
                nodes.add(new Node(position, department.getName(), position, worker.getEmail()));
            }
        }
        return nodes;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getByEmail(username);
    }
}
