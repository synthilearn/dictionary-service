package com.synthilearn.dictionaryservice.app.services.impl;

import com.synthilearn.dictionaryservice.app.port.DictionaryParametersRepository;
import com.synthilearn.dictionaryservice.app.port.DictionaryRepository;
import com.synthilearn.dictionaryservice.app.services.DictionaryService;
import com.synthilearn.dictionaryservice.domain.Dictionary;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.CreateDictionaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final DictionaryParametersRepository dictionaryParametersRepository;

    @Override
    @Transactional
    public Mono<Dictionary> createDictionary(CreateDictionaryDto request) {
        return dictionaryRepository.createDictionary(request.workareaId())
                .flatMap(dictionary -> dictionaryParametersRepository.initDictionary(dictionary.getId())
                        .thenReturn(dictionary));
    }
}
