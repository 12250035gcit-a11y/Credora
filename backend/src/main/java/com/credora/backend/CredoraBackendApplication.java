package com.credora.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point. Spring Boot auto-configures the embedded web server,
 * the database connection and the JPA layer from application.yml.
 */
@SpringBootApplication
public class CredoraBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CredoraBackendApplication.class, args);
    }
}
