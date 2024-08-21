package org.example.jwtauth.securityConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class config {
    private  ObjectMapper  objectMapper;

    @Bean
    public ObjectMapper getInstance(){
        return objectMapper;
    }
}
