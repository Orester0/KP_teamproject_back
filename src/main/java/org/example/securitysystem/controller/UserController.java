package org.example.securitysystem.controller;

import io.swagger.annotations.Api;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.User;
import org.example.securitysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "User API")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestParam String username) {
        return userService.registerUser(username);
    }

    @PostMapping("/login")
    public String login(@RequestParam String username) {
        return userService.loginUser(username);
    }

    @GetMapping("/accounts")
    public Map<String, User> getAllAccounts() {
        return userService.getAllUsers();
    }

    @GetMapping("/building/{username}")
    public Building getBuilding(@PathVariable String username) {
        return userService.getUserBuilding(username);
    }
}