package com.kapturecx.employeelogin.service;

import com.kapturecx.employeelogin.entity.EmployeeLogin;
import com.kapturecx.employeelogin.util.InvalidInputException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    @Autowired
    RedisService redisService;
    private final KafkaTemplate<String, EmployeeLogin> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, EmployeeLogin> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, EmployeeLogin employeeLogin) {
        kafkaTemplate.send(topic, employeeLogin);
    }

    @KafkaListener(topics = "employee-topic", groupId = "employee-group")
    // Handle received message
    // saving in redis
    public void listen(EmployeeLogin employeeLogin) throws InvalidInputException {
        redisService.saveInMap(employeeLogin);
        System.out.println("Received Message: " + employeeLogin.getUsername());
    }
}
