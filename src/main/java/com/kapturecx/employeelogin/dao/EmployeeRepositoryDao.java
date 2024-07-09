package com.kapturecx.employeelogin.dao;

import com.kapturecx.employeelogin.dto.EmployeeLoginDto;
import com.kapturecx.employeelogin.dto.EmployeeSignUpDto;
import com.kapturecx.employeelogin.util.HibernateUtil;
import com.kapturecx.employeelogin.entity.EmployeeLogin;
import com.kapturecx.employeelogin.validation.PasswordValidation;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class EmployeeRepositoryDao implements EmployeeRepository {

    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private HibernateUtil hibernateUtil;

    @Override
    public boolean saveEmployee(EmployeeLogin employeeLogin) {
        String password = PasswordValidation.encryptPassword(employeeLogin.getPassword());
        employeeLogin.setPassword(password);
        System.out.println(password);
        return HibernateUtil.saveOrUpdate(employeeLogin);
    }

    public boolean updateEmployee(EmployeeLogin employeeLogin) {
        return HibernateUtil.saveOrUpdate(employeeLogin);
    }

    @Override
    public EmployeeLogin findByUsernameAndPassword(String username, String password, int clientId) {
        String encryptedPassword = PasswordValidation.encryptPassword(password);
        String query = "FROM EmployeeLogin e WHERE e.username = :username AND e.password = :password AND e.clientId = :clientId";
        Map<String, Object> parametersSet = new HashMap<>();
        parametersSet.put("username", username);
        parametersSet.put("password", encryptedPassword);
        parametersSet.put("clientId", clientId);
        return HibernateUtil.getSingleResult(query, parametersSet, EmployeeLogin.class);
    }


    public EmployeeLogin findByIdAndClientId(int id) {
        String query = "From EmployeeLogin e where e.id = :id";
        Map<String, Object> parametersSet = new HashMap<>();
        parametersSet.put("id", id);
//        parametersSet.put("clientId", clientId);
        return HibernateUtil.getSingleResult(query, parametersSet, EmployeeLogin.class);
    }
    }


