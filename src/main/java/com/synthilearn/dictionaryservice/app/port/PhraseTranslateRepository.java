package com.synthilearn.dictionaryservice.app.port;

import com.synthilearn.dictionaryservice.domain.PhraseTranslate;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface PhraseTranslateRepository {

    Mono<List<PhraseTranslate>> updatePhraseTranslations(UUID phraseId, List<String> translations);
    Mono<List<PhraseTranslate>> findByPhraseId(UUID phraseId);
}
