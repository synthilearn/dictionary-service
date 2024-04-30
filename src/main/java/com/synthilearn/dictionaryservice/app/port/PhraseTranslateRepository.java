package com.synthilearn.dictionaryservice.app.port;

import java.util.List;
import java.util.UUID;

import com.synthilearn.dictionaryservice.domain.PhraseTranslate;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.TranslationShort;

import reactor.core.publisher.Mono;

public interface PhraseTranslateRepository {

    Mono<List<PhraseTranslate>> updatePhraseTranslations(UUID phraseId,
                                                         List<TranslationShort> translations);

    Mono<List<PhraseTranslate>> findByPhraseId(UUID phraseId);

    Mono<Void> deleteAll(UUID phraseId);

    Mono<PhraseTranslate> addTranslate(PhraseTranslate phraseTranslate);
}
