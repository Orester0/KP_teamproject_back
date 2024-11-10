package org.example.securitysystem.service;

import org.example.securitysystem.model.entity.User;
import org.example.securitysystem.model.entity.building.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            return "Username already taken.";
        }
        User newUser = new User(username);
        userRepository.save(newUser);
        return "User registered successfully.";
    }

    public String loginUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(value -> "Welcome " + value.getUsername() + "!").orElse("User not found.");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Building getUserBuilding(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return user.getBuilding();
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public Optional<Long> getUserIdByUsername(String username) {
        return userRepository.findByUsername(username).map(User::getId);
    }
}
