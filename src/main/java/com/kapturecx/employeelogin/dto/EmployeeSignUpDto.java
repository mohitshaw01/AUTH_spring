package com.kapturecx.employeelogin.dto;

import lombok.Data;

@Data
public class EmployeeSignUpDto {
    private int id;
    private int client_id;
    private String username;
    private String password;
    private Boolean enable;
    private int employeeId;
}
