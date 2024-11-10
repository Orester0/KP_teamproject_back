package org.example.securitysystem.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Ініціалізація фільтра, якщо потрібно
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Логування URL запиту та методу
        System.out.println("Incoming request: " + httpRequest.getMethod() + " " + httpRequest.getRequestURI());

        // Логування заголовків запиту
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = httpRequest.getHeader(headerName);
            System.out.println("Header: " + headerName + " = " + headerValue);
        }

        // Логування параметрів запиту
        String params = httpRequest.getParameterMap().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                .collect(Collectors.joining("&"));
        System.out.println("Parameters: " + params);

        chain.doFilter(request, response);

        // Логування статусу відповіді
        System.out.println("Response status: " + httpResponse.getStatus());
    }

    @Override
    public void destroy() {
        // Виконується при зупинці програми
    }
}