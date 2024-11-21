package org.example.securitysystem.controller;

import com.google.gson.Gson;
import org.example.securitysystem.model.dto.LogRequest;
import org.example.securitysystem.model.dto.SessionRequest;
import org.example.securitysystem.model.entity.Session;
import org.example.securitysystem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final SessionService sessionService;
    private final Gson gson;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
        this.gson = new Gson();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SessionRequest request) {
        try {
            Session session = sessionService.createSession(request.getName());
            return ResponseEntity.ok(gson.toJson(session));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(handleError(e));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody SessionRequest request) {
        try {
            Session session = sessionService.openSession(request.getName());
            return ResponseEntity.ok(gson.toJson(session));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(handleError(e));
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<String> getAllAccounts() {
        try {
            List<Session> sessions = sessionService.getAllSessions();
            return ResponseEntity.ok(gson.toJson(sessions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getlogs")
    public ResponseEntity<String> getLogs(LogRequest request) {
        try {
            //List<Session> sessions = sessionService.getAllSessions();
            String logs = "";
            return ResponseEntity.ok(gson.toJson(logs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String handleError(Exception e) {
        return "Error occurred: " + e.getMessage();
    }
}