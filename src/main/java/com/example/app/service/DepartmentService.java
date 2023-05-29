package com.example.app.service;

import com.example.app.model.Department;
import com.example.app.repository.DepartmentRepository;
import com.example.app.service.dto.DepartmentCreateRequest;
import com.example.app.service.dto.DepartmentNewEmployeeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final UserService userService;
    private final DepartmentRepository departmentRepository;

    public List<Department> getListOfAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department saveNewDepartment(DepartmentCreateRequest request) {
        var department = new Department();
        department.setName(request.getName());
        department.setHeadOfDepartment(userService.getByEmail(request.getEmailOfHeadOfDepartment()));
        return department;
    }

    public Department saveNewEmployeeToDepartment(DepartmentNewEmployeeRequest request) {
        var department = departmentRepository.findDepartmentByName(request.getDepartmentName());
        var user = userService.getByEmail(request.getEmailOfEmployee());
        department.setWorkers(List.of(user));
        user.setDepartments(List.of(department));
        return department;
    }
}
