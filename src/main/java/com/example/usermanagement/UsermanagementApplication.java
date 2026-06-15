package com.example.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UsermanagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsermanagementApplication.class, args);
    }
}
