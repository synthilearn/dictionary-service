package com.synthilearn.dictionaryservice.app.services;

import java.util.List;
import java.util.UUID;

import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.ChangeProgressRequest;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.InitPhraseRequest;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.PhraseTranslateEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PhraseService {

    Mono<Phrase> initPhrase(InitPhraseRequest request);

    Mono<Phrase> findByDictionaryAndText(UUID dictionaryId, String text);

    Mono<Object> findAll(GetAllPhraseRequestDto requestDto);

    Mono<Phrase> findById(UUID phraseId);

    Mono<Void> delete(UUID phraseId);

    Mono<List<Phrase>> findByPart(String textPart, UUID dictionaryId);

    Mono<Void> changeProgress(ChangeProgressRequest request);
}
