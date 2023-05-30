package com.example.app.service;

import com.example.app.model.Article;
import com.example.app.model.Department;
import com.example.app.model.User;
import com.example.app.repository.*;
import com.example.app.service.dto.Node;
import com.example.app.service.dto.RegisterRequest;
import com.example.app.service.dto.UpdateUserByAdminRequest;
import com.example.app.service.dto.UpdateUserRequest;
import com.example.app.service.mapper.MappingUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final ContactInfoRepository contactInfoRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MappingUtils mappingUtils;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public List<User> findAllUsers() {
        return userRepository.findAllByOrderById();
    }

    public Page<User> findAllPageableUsers(Pageable pageable) {
        List<User> users = findAllUsers();
        return findPaginated(pageable, users);
    }

    public Page<User> findAllPageableUsersByQuery(Pageable pageable, String query) {
        List<User> users = userRepository.findBySurnameOrNameOrPatronymicOrEmailContaining("%" + query + "%");
        return findPaginated(pageable, users);
    }

    public Page<User> findPaginated(Pageable pageable, List<User> users) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;

        if (users.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, users.size());
            list = users.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), users.size());
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
            user.getEmployee().setDateOfUpdate(LocalDateTime.now());
            return true;
        }
        return false;
    }

    public User updateUser(String login, UpdateUserRequest updateUserRequest) {
        var user = getByEmail(login);
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

    public User updateUserByAdmin(String email, UpdateUserByAdminRequest request) {
        var user = getByEmail(email);
        if (Strings.isNotBlank(request.getEmail())) {
            var existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("Email is already registered: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        user.setRoles(request.getRoles());
        if (Strings.isNotBlank(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.getEmployee().setPosition(request.getPosition());
        user.setDepartments(request.getDepartment());
        user.getEmployee().setDateOfUpdate(LocalDateTime.now());
        return user;
    }

    public boolean activateUserAccount(String email) {
        var user = getByEmail(email);
        if (!user.getActive()) {
            user.setActive(true);
            user.getEmployee().setDateOfUpdate(LocalDateTime.now());
            return true;
        }
        return false;
    }

    public List<Article> getListOfAllArticlesForUser(String email) {
        var user = getByEmail(email);
        return articleRepository.findAllByIsModeratedIsTrueAndIsPublishedIsTrueAndAuthor(user);
    }

    public List<Integer> getListOfPageNumbersForPagination(Page userPage) {
        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            return IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
        } else return null;
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