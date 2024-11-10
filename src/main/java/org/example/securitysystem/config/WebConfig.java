package org.example.securitysystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Дозволяємо доступ до всіх ендпоінтів з конкретного джерела
        registry.addMapping("/**").allowedOrigins("http://127.0.0.1:5500");
    }
}
