package com.synthilearn.dictionaryservice.infra.api.rest.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.infra.api.rest.exception.parent.GenericException;

public class PhraseException extends GenericException {

    public PhraseException(String message, HttpStatus status, Integer code) {
        super(message, status, code);
    }

    public static PhraseException notFound(UUID phraseId) {
        return new PhraseException(String.format("Phrase with phraseId: %s not found", phraseId),
                HttpStatus.NOT_FOUND, 1000);
    }

    public static PhraseException notFoundByPartAndText(UUID dictionaryId, String text) {
        return new PhraseException(
                String.format("Phrase for dictionary: %s and text: %s not found",
                        dictionaryId, text), HttpStatus.NOT_FOUND, 1000);
    }

    public static PhraseException alreadyExists(String text, PartOfSpeech part) {
        return new PhraseException(
                String.format("Phrase with text: %s and part of speech: %s already defined",
                        text, part), HttpStatus.BAD_REQUEST, 1000);
    }
}
