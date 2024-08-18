package org.example.jwtauth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class config {
    private  ObjectMapper  objectMapper;


//    public config(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }


    @Bean
    public ObjectMapper getInstance(){
        return objectMapper;
    }
}
