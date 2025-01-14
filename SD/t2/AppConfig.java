package com.example.trabalho_t2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    // Definir o RestTemplate como um Bean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
