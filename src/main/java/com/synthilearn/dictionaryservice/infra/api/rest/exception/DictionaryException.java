package com.synthilearn.dictionaryservice.infra.api.rest.exception;

import com.synthilearn.dictionaryservice.infra.api.rest.exception.parent.GenericException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class DictionaryException extends GenericException {
    public DictionaryException(String message, HttpStatus status, Integer code) {
        super(message, status, code);
    }

    public static DictionaryException alreadyExists(UUID workareaId) {
        return new DictionaryException(String.format("Dictionary for workarea: %s already exists", workareaId), HttpStatus.BAD_REQUEST, 1000);
    }
}
