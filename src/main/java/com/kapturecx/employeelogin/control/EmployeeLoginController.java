package com.kapturecx.employeelogin.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kapturecx.employeelogin.dto.EmployeeLoginDto;
import com.kapturecx.employeelogin.dto.EmployeeSignUpDto;
import com.kapturecx.employeelogin.service.EmployeeLoginService;
import com.kapturecx.employeelogin.service.SessionService;
import com.kapturecx.employeelogin.util.InvalidInputException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.runtime.ObjectMethods;

@RestController
@RequestMapping("/api/auth")
public class EmployeeLoginController {
    @Autowired
    EmployeeLoginService employeeLoginService;
    @Autowired
    SessionService sessionService;

    @Autowired
    @Qualifier("employee")
    ObjectMapper objectMapper;

    @PostMapping("/signin")
    public ResponseEntity<ObjectNode> logIn(@RequestBody EmployeeLoginDto employeeLoginDto, HttpServletRequest request, HttpServletResponse response)throws InvalidInputException {
        return employeeLoginService.login(employeeLoginDto,request,response);
    }
    @PostMapping("/signup")
    public ResponseEntity<ObjectNode> signUp(@RequestBody EmployeeSignUpDto employeeSignUpDto) throws InvalidInputException{
        return employeeLoginService.signup(employeeSignUpDto);
    }
    @PostMapping("/signout")
    public ResponseEntity<ObjectNode> logOut(HttpServletRequest request) throws InvalidInputException{
        return employeeLoginService.logout(request);
    }
}
