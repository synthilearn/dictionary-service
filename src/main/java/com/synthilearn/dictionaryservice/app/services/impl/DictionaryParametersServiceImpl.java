package com.synthilearn.dictionaryservice.app.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.synthilearn.dictionaryservice.app.port.DictionaryParametersRepository;
import com.synthilearn.dictionaryservice.app.port.DictionaryRepository;
import com.synthilearn.dictionaryservice.app.services.DictionaryParametersService;
import com.synthilearn.dictionaryservice.app.services.DictionaryService;
import com.synthilearn.dictionaryservice.domain.DictionaryParameters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DictionaryParametersServiceImpl implements DictionaryParametersService {

    private final DictionaryParametersRepository dictionaryParametersRepository;


    @Override
    public Mono<DictionaryParameters> findByDictionary(UUID dictionaryId) {
        return dictionaryParametersRepository.findByDictionaryId(dictionaryId);
    }
}
