//package org.example.securitysystem.controller;
//
//import org.example.securitysystem.service.SessionService;
//import org.example.securitysystem.service.WebSocketService;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.AbstractWebSocketHandler;
//
//@Component
//public class WebSocketEventHandler extends AbstractWebSocketHandler {
//    private final SessionService sessionService;
//    private final WebSocketService webSocketService;
//
//    public WebSocketEventHandler(SessionService sessionService, WebSocketService webSocketService) {
//        this.sessionService = sessionService;
//        this.webSocketService = webSocketService;
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//        // Отримуємо ID сесії з атрибутів WebSocket сесії
//        String sessionId = session.getAttributes().get("sessionId").toString();
//
//        // Виконуємо необхідні дії при відключенні
//        handleClientDisconnect(sessionId);
//    }
//
//    private void handleClientDisconnect(String sessionId) {
//        try {
//            // Очищення ресурсів
//            webSocketService.removeTopicForSession(Long.parseLong(sessionId));
//            // Можливо зупинити симуляцію
//            // simulationService.stopSimulation(sessionId);
//
//            // Логування відключення
//            System.out.println("Client disconnected from session: " + sessionId);
//        } catch (Exception e) {
//            System.err.println("Error handling client disconnect: " + e.getMessage());
//        }
//    }
//}