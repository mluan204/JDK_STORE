package com.example.Backend_IE303;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendIe303Application {


	public static void main(String[] args) {
		SpringApplication.run(BackendIe303Application.class, args);
	}


}