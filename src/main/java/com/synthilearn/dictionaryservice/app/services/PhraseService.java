package com.synthilearn.dictionaryservice.app.services;

import java.util.List;
import java.util.UUID;

import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.InitPhraseRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PhraseService {

    Mono<Phrase> initPhrase(InitPhraseRequest request);
    Mono<Phrase> findByPartAndText(PartOfSpeech part, String text);
    Mono<Object> findAll(GetAllPhraseRequestDto requestDto);
}
