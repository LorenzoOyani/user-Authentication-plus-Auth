package org.example.jwtauth.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class CommonSubError implements Serializable {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String field;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Object value;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String type;


}

