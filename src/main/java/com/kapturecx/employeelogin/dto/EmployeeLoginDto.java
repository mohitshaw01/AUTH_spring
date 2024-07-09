package com.kapturecx.employeelogin.dto;

import lombok.Data;

@Data
public class EmployeeLoginDto {
    private int client_id;
    private String username;
    private String password;
    private boolean enable;
    private int employeeId;
    public EmployeeLoginDto() {}
}
