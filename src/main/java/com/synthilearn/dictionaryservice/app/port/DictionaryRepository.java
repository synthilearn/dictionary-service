package com.synthilearn.dictionaryservice.app.port;

import com.synthilearn.dictionaryservice.domain.Dictionary;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DictionaryRepository {
    Mono<Dictionary> createDictionary(UUID workareaId);
}
