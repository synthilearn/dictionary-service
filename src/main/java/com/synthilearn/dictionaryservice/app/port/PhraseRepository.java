package com.synthilearn.dictionaryservice.app.port;

import java.util.UUID;

import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.Phrase;

import reactor.core.publisher.Mono;

public interface PhraseRepository {

    Mono<Phrase> initPhrase(Phrase phrase);

    Mono<Phrase> findById(UUID phraseId);

    Mono<Phrase> findByTextAndType(String text, PartOfSpeech part);
}
