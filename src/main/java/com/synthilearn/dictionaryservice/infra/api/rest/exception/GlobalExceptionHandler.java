package com.synthilearn.dictionaryservice.infra.api.rest.exception;

import com.synthilearn.commonstarter.GenericResponse;
import com.synthilearn.dictionaryservice.infra.api.rest.exception.parent.GenericException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<GenericResponse<?>> handleValidationException(WebExchangeBindException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return Mono.just(GenericResponse.validError(errors.toString()));
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<Mono<GenericResponse<?>>> handleUserAlreadyExistsException(GenericException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(Mono.just(GenericResponse.error(ex.getCode(), ex.getMessage())));
    }
}
