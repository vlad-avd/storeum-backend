package com.avdienko.storeum.payload.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class GenericResponse<T> {

    private T body;
    private String errorMessage;
    private HttpStatus statusCode;

    @SneakyThrows
    public String getResponseBody() {
        ObjectMapper mapper = new ObjectMapper();
        return body == null
                ? errorMessage
                : mapper.writeValueAsString(body);
    }
}
