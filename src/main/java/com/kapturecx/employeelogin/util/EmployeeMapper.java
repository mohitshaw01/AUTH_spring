package com.kapturecx.employeelogin.util;

import com.kapturecx.employeelogin.dto.EmployeeLoginDto;
import com.kapturecx.employeelogin.entity.EmployeeLogin;

public class EmployeeMapper {
    public static EmployeeLoginDto employeetoDTO(EmployeeLogin employeeLogin){
        EmployeeLoginDto employeeLoginDto = new EmployeeLoginDto();
        employeeLoginDto.setClient_id(employeeLogin.getClientId());
        employeeLoginDto.setUsername(employeeLogin.getUsername());
        employeeLoginDto.setPassword(employeeLogin.getPassword());
        return employeeLoginDto;
    }
    public static EmployeeLogin dtoToEmployee(EmployeeLoginDto employeeLoginDto){
        EmployeeLogin employeeLogin = new EmployeeLogin();
        employeeLogin.setClientId(employeeLoginDto.getClient_id());
        employeeLogin.setUsername(employeeLoginDto.getUsername());
        employeeLogin.setPassword(employeeLoginDto.getPassword());
        return employeeLogin;
    }
}
