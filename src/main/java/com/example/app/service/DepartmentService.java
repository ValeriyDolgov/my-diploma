package com.example.app.service;

import com.example.app.model.Department;
import com.example.app.model.User;
import com.example.app.repository.DepartmentRepository;
import com.example.app.repository.UserRepository;
import com.example.app.service.dto.DepartmentCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public List<Department> getListOfAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentByName(String name) {
        return departmentRepository.findDepartmentByName(name);
    }

    public Department saveNewDepartment(DepartmentCreateRequest request) {
        var department = new Department();
        department.setName(request.getName());
        department.setHeadOfDepartment(userService.getByEmail(request.getEmailOfHeadOfDepartment()));
        departmentRepository.save(department);
        return department;
    }

    public Department saveNewEmployeeToDepartment(String departmentName, String emailOfWorker) {
        var department = departmentRepository.findDepartmentByName(departmentName);
        List<User> departmentWorkers = department.getWorkers();
        var user = userService.getByEmail(emailOfWorker);
        List<Department> userDepartments = user.getDepartments();
        if (!user.getIsCEO() && user.getActive()) {
            if (!departmentWorkers.contains(user)) {
                departmentWorkers.add(user);
                department.setWorkers(departmentWorkers);
                departmentRepository.save(department);
            }
            if (!userDepartments.contains(department)) {
                userDepartments.add(department);
                user.setDepartments(userDepartments);
                userRepository.save(user);
            }
        }
        userRepository.save(user);
        return department;
    }
}
