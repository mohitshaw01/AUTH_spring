package com.kapturecx.employeelogin.service;

import com.kapturecx.employeelogin.dao.EmployeeRepository;
import com.kapturecx.employeelogin.entity.EmployeeLogin;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);
    private static final int SESSION_TIMEOUT_SECONDS = 60; // 1 minute

    private final ConcurrentHashMap<Integer, Set<HttpSession>> userSessions = new ConcurrentHashMap<>();

    public void createSession(HttpServletRequest request, HttpServletResponse response, int id, String username, String password, int clientId) {
        HttpSession session = request.getSession(true);
        session.setAttribute("id", id);
        session.setAttribute("username", username);
        session.setAttribute("clientId", clientId);
        session.setAttribute("sessionStartTime", System.currentTimeMillis());
        session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);

        createCookie(response, session);

        Set<HttpSession> sessions = userSessions.computeIfAbsent(id, k -> Collections.newSetFromMap(new ConcurrentHashMap<>()));
        sessions.add(session);

        logger.info("Session created for user id: {}", id);

    }

    private static void createCookie(HttpServletResponse response, HttpSession session) {
        Cookie sessionCookie = new Cookie("KaptureCX", session.getId());
        sessionCookie.setMaxAge(SESSION_TIMEOUT_SECONDS);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
    }

    public boolean invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.error("No session found for the request.");
            return false;
        }
        try {
                Integer id = (Integer) session.getAttribute("clientId");
                logger.info("{}",id);
                EmployeeLogin employeeLogin = employeeRepository.findByIdAndClientId(id);
                userSessions.remove(id);
                session.invalidate();
                employeeLogin.setActiveLogin(false);
                employeeRepository.updateEmployee(employeeLogin);
                logger.info("Session invalidated for user id: {}", id);
                return true;
        } catch (Exception e) {
            logger.error("Error during session invalidation", e);
            return false;
        }
    }
}