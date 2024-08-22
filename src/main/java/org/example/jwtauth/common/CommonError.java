package org.example.jwtauth.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Builder
public class CommonError {

    @Builder.Default
    private LocalDateTime dateTime = LocalDateTime.now();

    private HttpStatus  httpStatus;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    private String header;

    @Builder.Default
    private boolean isSuccess = false;

    private List<CommonSubError> subErrorList;





    @Getter
    @RequiredArgsConstructor
    public enum Header{

        AUTH_ERROR("AUTH_ERROR", "Authentication error!");


        private String  value;

        private String  message;

        Header(String  value,  String message){
            this.value=value;
            this.message  =message;
        }

    }

}
