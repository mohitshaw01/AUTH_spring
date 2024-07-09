package com.kapturecx.employeelogin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kapturecx.employeelogin.dao.EmployeeRepository;
import com.kapturecx.employeelogin.dto.EmployeeLoginDto;
import com.kapturecx.employeelogin.dto.EmployeeSignUpDto;
import com.kapturecx.employeelogin.entity.EmployeeLogin;
import com.kapturecx.employeelogin.util.EmployeeMapper;
import com.kapturecx.employeelogin.util.EmployeeSignUpMapper;
import com.kapturecx.employeelogin.util.InvalidInputException;
import com.kapturecx.employeelogin.validation.SignUpValidation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.kapturecx.employeelogin.constant.Constants.*;

@Service
public class EmployeeLoginService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private RedisService redisService;

    @Autowired
    @Qualifier("employee")
    ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);

    public ResponseEntity<ObjectNode> login(EmployeeLoginDto employeeLoginDto, HttpServletRequest request, HttpServletResponse response) throws InvalidInputException {
        ObjectNode responseObject = objectMapper.createObjectNode();
        EmployeeLogin employeeLogin = EmployeeMapper.dtoToEmployee(employeeLoginDto);
        String username = employeeLogin.getUsername();
        String password = employeeLogin.getPassword();
        int clientId = employeeLogin.getClientId();
        EmployeeLogin foundEmployee = employeeRepository.findByUsernameAndPassword(username, password, clientId);
        if (foundEmployee == null) {
            responseObject.put("status", false);
            responseObject.put("message", ERROR_USERNAME_NOT_FOUND);
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
        }

        if (username.isEmpty() || password.isEmpty() || clientId <= 0) {
            responseObject.put("status", false);
            responseObject.put("message", INVALID_USERNAME_OR_PASSWORD);
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
        }
        // Check Redis cache first
        EmployeeLogin cachedEmployee = redisService.getFromMapByEmployeeId(foundEmployee.getEmployeeId());

        if (cachedEmployee != null && cachedEmployee.getPassword().equals(password) && cachedEmployee.getClientId() == clientId) {
            responseObject.put("status", true);
            responseObject.put("message", SUCCESS_LOGIN);
            sessionService.createSession(request, response, clientId, username, password, cachedEmployee.getId());
            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        }

        boolean enable = foundEmployee.isEnable();
        if (!enable) {
            responseObject.put("status", false);
            responseObject.put("message", USER_DISABLED);
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
        }
        responseObject.put("status", true);
        responseObject.put("message", SUCCESS_LOGIN);
        foundEmployee.setActiveLogin(true);
        int id = foundEmployee.getId();
        sessionService.createSession(request, response, clientId, username, password, id);

        // Cache the employee in Redis


        if (employeeRepository.updateEmployee(foundEmployee)) {
            kafkaService.sendMessage("employee-login-topic", employeeLogin);
            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        }
    }
    // SIGNUP
    public ResponseEntity<ObjectNode> signup(EmployeeSignUpDto employeeSignUpDto) {
        ObjectNode responseObject = objectMapper.createObjectNode();
        EmployeeLogin employeeLogin = EmployeeSignUpMapper.dtoToEmployee(employeeSignUpDto);
        String username = employeeLogin.getUsername();
        String password = employeeLogin.getPassword();
        int clientId = employeeLogin.getClientId();
        int employeeId = employeeLogin.getEmployeeId();
        logger.info("{}",employeeId);
        if (!SignUpValidation.validateSignUp(username, password, clientId, employeeId)) {
            responseObject.put("status", false);
            responseObject.put("message", ERROR_INVALID_CREDENTIALS);
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
        }

        try {
            employeeLogin.setEmployeeId(employeeId);
            if (!employeeRepository.saveEmployee(employeeLogin)) {
                return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
            }
            responseObject.put("status", true);
            responseObject.put("message", SUCCESS_SIGNUP);
            kafkaService.sendMessage("employee-topic", employeeLogin);
            logger.info("SignUp successfully");
            return ResponseEntity.status(200).body(responseObject);
        } catch (Exception e) {
            responseObject.put("status", false);
            responseObject.put("message", ERROR_SIGNUP_FAILED);
            return ResponseEntity.status(500).body(responseObject);
        }
    }
    // LOGOUT
    public ResponseEntity<ObjectNode> logout(HttpServletRequest request) {
        ObjectNode responseObject = objectMapper.createObjectNode();
        try {
            if (request == null) {
                throw new NullPointerException("HttpServletRequest is null");
            }
            if (sessionService.invalidateSession(request)) {
                responseObject.put("message", "Successfully logged out");
                return new ResponseEntity<>(responseObject, HttpStatus.OK);
            } else {
                responseObject.put("error", "Error logging out");
                return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            responseObject.put("error", "Request cannot be null");
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            responseObject.put("error", "Session is already invalidated or does not exist");
            return new ResponseEntity<>(responseObject, HttpStatus.CONFLICT);
        } catch (Exception e) {
            responseObject.put("error", "Unexpected error: " + e.getMessage());
            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}