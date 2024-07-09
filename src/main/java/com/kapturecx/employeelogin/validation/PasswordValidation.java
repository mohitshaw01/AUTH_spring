package com.kapturecx.employeelogin.validation;
import java.util.Base64;
public class PasswordValidation {
    // use to encrypt the password using Base64 or we can use sprint security as well.
    public static String encryptPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
}
