package com.kapturecx.employeelogin.service;

import com.kapturecx.employeelogin.entity.EmployeeLogin;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedissonClient redissonClient;
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    private final String employeeLoginBucketKey = "EMPID:EmployeeLogin";

    public void saveInMap(EmployeeLogin employeeLogin) {
        try {
            RBucket<EmployeeLogin> loginRBucket = redissonClient.getBucket(employeeLoginBucketKey + employeeLogin.getEmployeeId());
            loginRBucket.set(employeeLogin);
        } catch (Exception e) {
            //handle the exception appropriately
            e.printStackTrace();
        }
    }

    public EmployeeLogin getFromMapByEmployeeId(int empId) {
        try {
            RBucket<EmployeeLogin> loginRBucket = redissonClient.getBucket(employeeLoginBucketKey + empId);
            return loginRBucket.get();
        } catch (Exception e) {
            //  handle the exception appropriately
            e.printStackTrace();
            return null; // Or throw an exception as needed
        }
    }
}
