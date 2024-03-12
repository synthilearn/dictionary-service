package com.synthilearn.dictionaryservice.app.port;

import com.synthilearn.dictionaryservice.domain.Dictionary;
import com.synthilearn.dictionaryservice.domain.DictionaryParameters;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.DictionaryParametersEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DictionaryParametersRepository {

    Mono<DictionaryParameters> initDictionary(UUID workareaId);
}
