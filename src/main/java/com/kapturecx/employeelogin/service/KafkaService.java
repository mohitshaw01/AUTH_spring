package com.kapturecx.employeelogin.service;

import com.kapturecx.employeelogin.entity.EmployeeLogin;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final KafkaTemplate<String, EmployeeLogin> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, EmployeeLogin> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, EmployeeLogin employeeLogin) {
        kafkaTemplate.send(topic, employeeLogin);
    }

    @KafkaListener(topics = "employee-topic", groupId = "employee-group")
    // Handle received message
    public void listen(EmployeeLogin employeeLogin) {
        System.out.println("Received Message: " + employeeLogin.getUsername());
    }
}