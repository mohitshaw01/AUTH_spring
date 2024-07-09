package com.kapturecx.employeelogin.util;

import com.kapturecx.employeelogin.dto.EmployeeSignUpDto;
import com.kapturecx.employeelogin.entity.EmployeeLogin;

public class EmployeeSignUpMapper {
    public static EmployeeSignUpDto employeeToDto(EmployeeLogin employeeLogin) {
        EmployeeSignUpDto employeeSignUpDto = new EmployeeSignUpDto();
        employeeSignUpDto.setId(employeeLogin.getId());
        employeeSignUpDto.setClient_id(employeeLogin.getClientId());
        employeeSignUpDto.setUsername(employeeLogin.getUsername());
        employeeSignUpDto.setPassword(employeeLogin.getPassword());
        employeeSignUpDto.setEnable(employeeLogin.isEnable());
        employeeSignUpDto.setEmployeeId(employeeLogin.getEmployeeId());
        return employeeSignUpDto;
    }
    public static EmployeeLogin dtoToEmployee(EmployeeSignUpDto employeeSignUpDto) {
        EmployeeLogin employeeLogin = new EmployeeLogin();
        employeeLogin.setId(employeeSignUpDto.getId());
        employeeLogin.setClientId(employeeSignUpDto.getClient_id());
        employeeLogin.setUsername(employeeSignUpDto.getUsername());
        employeeLogin.setPassword(employeeSignUpDto.getPassword());
        employeeLogin.setEnable(employeeSignUpDto.getEnable());
        employeeLogin.setEmployeeId(employeeSignUpDto.getEmployeeId());
        return employeeLogin;
    }
}
