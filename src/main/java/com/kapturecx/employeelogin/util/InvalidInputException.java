package com.kapturecx.employeelogin.util;

import java.io.Serial;

public class InvalidInputException extends  Exception{
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message);
    }
}
