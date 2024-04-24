package com.synthilearn.dictionaryservice.app.port;

import java.util.UUID;

import com.synthilearn.dictionaryservice.domain.DictionaryParameters;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;

import reactor.core.publisher.Mono;

public interface DictionaryParametersRepository {

    Mono<DictionaryParameters> initDictionary(UUID workareaId);

    Mono<DictionaryParameters> editParameters(GetAllPhraseRequestDto requestDto);

    Mono<DictionaryParameters> findByDictionaryId(UUID dictionaryId);
}
