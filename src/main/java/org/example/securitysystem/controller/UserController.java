package org.example.securitysystem.controller;

import com.google.gson.Gson;
import org.example.securitysystem.model.dto.UserRequest;
import org.example.securitysystem.model.entity.User;
import org.example.securitysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final Gson gson;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.gson = new Gson();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest request) {
        try {
            User user = userService.registerUser(request.getUsername());
            return ResponseEntity.ok(gson.toJson(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(handleError(e));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequest request) {
        try {
            User user = userService.loginUser(request.getUsername());
            return ResponseEntity.ok(gson.toJson(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(handleError(e));
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<String> getAllAccounts() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(gson.toJson(users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String handleError(Exception e) {
        return "Error occurred: " + e.getMessage();
    }
}