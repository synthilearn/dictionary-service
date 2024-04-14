package com.synthilearn.dictionaryservice.app.services;

import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.InitPhraseRequest;
import reactor.core.publisher.Mono;

public interface PhraseService {

    Mono<Phrase> initPhrase(InitPhraseRequest request);
    Mono<Phrase> findByPartAndText(PartOfSpeech part, String text);
}
