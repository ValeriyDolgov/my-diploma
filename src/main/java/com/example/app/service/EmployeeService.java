package com.example.app.service;

import com.example.app.model.Employee;
import com.example.app.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public Employee getById(Long id) {
        return employeeRepository.findEmployeeById(id);
    }
}
