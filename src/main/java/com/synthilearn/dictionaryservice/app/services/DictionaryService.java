package com.synthilearn.dictionaryservice.app.services;

import com.synthilearn.dictionaryservice.domain.Dictionary;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.CreateDictionaryDto;
import reactor.core.publisher.Mono;

public interface DictionaryService {

    Mono<Dictionary> createDictionary(CreateDictionaryDto request);
}
