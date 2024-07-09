package com.kapturecx.employeelogin.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "employee_login")
public class EmployeeLogin implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "client_id", nullable = false)
    private int clientId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password; // Store encrypted password

    @UpdateTimestamp
    @Column(name = "last_login_time", nullable = true)
    private Timestamp lastLoginTime;

    @UpdateTimestamp
    @Column(name = "last_password_reset", nullable = true)
    private Timestamp lastPasswordReset;

    @Column(name = "enable", nullable = false)
    private boolean enable;

    @Column(name = "active_login", nullable = false)
    private boolean activeLogin = false;

    @Column(name = "employee_id" , nullable = false)
    private int employeeId;
}
