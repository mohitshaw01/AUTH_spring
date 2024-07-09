package com.kapturecx.employeelogin.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class objectMapperConfig {
    @Bean
    @Primary
    @Qualifier("employee")
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}