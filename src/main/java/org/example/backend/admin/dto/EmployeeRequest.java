package org.example.backend.admin.dto;

import lombok.Data;

@Data
public class EmployeeRequest {
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String image;
}
