package com.example.app.service;

import com.example.app.model.Employee;
import com.example.app.repository.EmployeeRepository;
import com.example.app.service.mapper.MappingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final MappingUtils mappingUtils;

    public Employee getById(Long id){
        return employeeRepository.findEmployeeById(id);
    }
}
