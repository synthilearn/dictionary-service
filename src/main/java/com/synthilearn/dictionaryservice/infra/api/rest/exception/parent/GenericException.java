package com.synthilearn.dictionaryservice.infra.api.rest.exception.parent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class GenericException extends RuntimeException {

    private final HttpStatus status;
    private final Integer code;

    public GenericException(String message, HttpStatus status, Integer code) {
        super(message);
        this.status = status;
        this.code = code;
    }
}