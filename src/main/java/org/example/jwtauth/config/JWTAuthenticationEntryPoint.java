package org.example.jwtauth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.jwtauth.common.CommonError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.text.DateFormat;

@Slf4j
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JWTAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        log.error("AuthEntryPoint || commence || Auth error: {} ", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        CommonError commonError = CommonError.builder()
                .header(CommonError.Header.AUTH_ERROR.getValue())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .isSuccess(false)
                .build();


        String responseBody = objectMapper
                .writer(DateFormat.getDateInstance())
                .writeValueAsString(commonError);


        response.getOutputStream()
                .write(responseBody.getBytes());


    }
}
