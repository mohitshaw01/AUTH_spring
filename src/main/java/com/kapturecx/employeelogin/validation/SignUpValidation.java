package com.kapturecx.employeelogin.validation;

public class SignUpValidation {
    public static boolean validateSignUp(String username, String password, int clientId, int employeeId) {
        return !username.isEmpty() && !password.isEmpty() && clientId > 0 && employeeId > 0;
    }
}
