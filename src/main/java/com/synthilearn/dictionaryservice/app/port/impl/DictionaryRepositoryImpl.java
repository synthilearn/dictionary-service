package com.synthilearn.dictionaryservice.app.port.impl;

import com.synthilearn.dictionaryservice.app.port.DictionaryRepository;
import com.synthilearn.dictionaryservice.domain.Dictionary;
import com.synthilearn.dictionaryservice.infra.api.rest.exception.DictionaryException;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.DictionaryEntity;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper.DictionaryEntityMapper;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.repository.DictionaryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DictionaryRepositoryImpl implements DictionaryRepository {

    private final DictionaryEntityMapper dictionaryEntityMapper;
    private final DictionaryJpaRepository dictionaryJpaRepository;

    @Override
    @Transactional
    public Mono<Dictionary> createDictionary(UUID workareaId) {
        return dictionaryJpaRepository.findByWorkareaId(workareaId)
                .singleOptional()
                .flatMap(dictionaryOpt -> {
                    if (dictionaryOpt.isPresent()) {
                        return Mono.error(DictionaryException.alreadyExists(workareaId));
                    }
                    return dictionaryJpaRepository.save(DictionaryEntity.builder()
                            .id(UUID.randomUUID())
                            .workareaId(workareaId)
                            .newRecord(true)
                            .build()
                    ).map(dictionaryEntityMapper::map);
                });
    }
}
