package com.cherry.jeeves;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JeevesApplication {

    public static void main(String[] args) {
        SpringApplication.run(JeevesApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(Jeeves jeeves) throws Exception {
        return args -> jeeves.start();
    }
}
