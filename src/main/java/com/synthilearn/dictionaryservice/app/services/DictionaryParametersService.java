package com.synthilearn.dictionaryservice.app.services;

import java.util.UUID;

import com.synthilearn.dictionaryservice.domain.DictionaryParameters;

import reactor.core.publisher.Mono;

public interface DictionaryParametersService {
    Mono<DictionaryParameters> findByDictionary(UUID uuid);
}
