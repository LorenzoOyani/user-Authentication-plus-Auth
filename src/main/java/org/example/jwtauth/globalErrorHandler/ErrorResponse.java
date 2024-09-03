package org.example.jwtauth.globalErrorHandler;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
@Getter

public class ErrorResponse {

    @Builder.Default
    private LocalDateTime localDateTime = LocalDateTime.now();

    private List<String> messages;

    public ErrorResponse(String messages) {
        this.localDateTime = LocalDateTime.now();
        this.messages.add(messages);
    }


};
