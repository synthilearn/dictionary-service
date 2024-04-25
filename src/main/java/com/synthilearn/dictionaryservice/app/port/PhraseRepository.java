package com.synthilearn.dictionaryservice.app.port;

import java.util.List;
import java.util.UUID;

import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;

import reactor.core.publisher.Mono;

public interface PhraseRepository {

    Mono<Phrase> initPhrase(Phrase phrase);

    Mono<Phrase> findById(UUID phraseId);

    Mono<Phrase> findByText(String text, UUID dictionaryId);

    Mono<List<Phrase>> findAll(GetAllPhraseRequestDto requestDto);

    Mono<Void> delete(UUID phraseId);

    Mono<List<Phrase>> findByTextPart(UUID dictionaryId, String textPart);
}
