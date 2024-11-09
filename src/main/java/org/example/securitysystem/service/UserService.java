package org.example.securitysystem.service;

import org.example.securitysystem.model.entity.User;
import org.example.securitysystem.model.entity.building.Building;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, User> userDatabase = new HashMap<>();

    public String registerUser(String username) {
        if (userDatabase.containsKey(username)) {
            return "Username already taken.";
        }
        User newUser = new User(username);
        userDatabase.put(username, newUser);
        return "User registered successfully.";
    }

    public String loginUser(String username) {
        if (!userDatabase.containsKey(username)) {
            return "User not found.";
        }
        return "Welcome " + username + "!";
    }

    public Map<String, User> getAllUsers() {
        return userDatabase;
    }

    public Building getUserBuilding(String username) {
        User user = userDatabase.get(username);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        return user.getBuilding();
    }

    public User getUser(Long userId) {
        return userDatabase.get(userId);
    }

    public void updateUser(User user) {
        userDatabase.put(String.valueOf(user.getId()), user); // Зберігаємо оновлену інформацію про користувача
    }

}
