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



    @Builder
    private static class CommonSubError{

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String field;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Object value;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String type;


    }

    @Getter
    @RequiredArgsConstructor
    public enum Header{

        AUTH_ERROR("AUTH_ERROR");


        private String  value;

        Header(String  value){
            this.value=value;
        }

    }

}
