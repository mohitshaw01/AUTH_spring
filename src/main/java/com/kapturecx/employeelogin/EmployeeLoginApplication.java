package com.kapturecx.employeelogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableCaching
public class EmployeeLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeLoginApplication.class, args);
	}

}
