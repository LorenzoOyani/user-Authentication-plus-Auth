package org.example.jwtauth;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.stream.Collectors;

@SpringBootApplication
public class JwtAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtAuthApplication.class, args);

        String greetings = "Hello World";

//        String returnedValue = greetings.chars()
//                .mapToObj(obj -> (char) obj)
//                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
//                    Collections.reverse(list);
//                    return list.stream();
//
//                }))
//                .map(String::valueOf)
//                .collect(Collectors.joining(" "));
//
//
//        System.out.println(returnedValue);


    }

}
