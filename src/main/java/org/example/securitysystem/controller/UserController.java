package org.example.securitysystem.controller;

import org.example.securitysystem.model.dto.UserRequest;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.User;
import org.example.securitysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/user/register")
    @SendTo("/topic/user")
    public String register(UserRequest request) {
        try {
            return userService.registerUser(request.getUsername());
        } catch (Exception e) {
            return handleError(e);
        }
    }

    @MessageMapping("/user/login")
    @SendTo("/topic/user")
    public String login(UserRequest request) {
        try {
            return userService.loginUser(request.getUsername());
        } catch (Exception e) {
            return handleError(e);
        }
    }

    @MessageMapping("/user/accounts")
    @SendTo("/topic/accounts")
    public List<User> getAllAccounts() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            // Тут ви можете визначити, який тип помилки повертати для цього методу
            throw new RuntimeException("Error retrieving user accounts: " + e.getMessage());
        }
    }

    // Метод для обробки помилок
    private String handleError(Exception e) {
        // Можна додати більш детальну обробку залежно від типу винятку
        return "Error occurred: " + e.getMessage();
    }
}