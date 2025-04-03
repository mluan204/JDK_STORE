package com.example.Backend_IE303.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Cho phép tất cả API trong /api/
                        .allowedOrigins("http://localhost:5173") // Cho phép frontend truy cập
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Cho phép các method HTTP
                        .allowedHeaders("*")
                        .allowCredentials(true); // Nếu dùng cookies hoặc xác thực
            }
        };
    }
}
