package com.example.app.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentNewEmployeeRequest {
    private String departmentName;
    private String emailOfEmployee;
}
