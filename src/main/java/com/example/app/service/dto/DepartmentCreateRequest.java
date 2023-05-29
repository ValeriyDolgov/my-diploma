package com.example.app.service.dto;

import lombok.Data;

@Data
//@Builder
public class DepartmentCreateRequest {
    private String name;
    private String emailOfHeadOfDepartment;
}
