package com.cherry.jeeves.utils.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpRestConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new StatefullRestTemplate();
    }
}