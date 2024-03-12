package com.synthilearn.dictionaryservice.app.port.impl;

import com.synthilearn.dictionaryservice.app.port.DictionaryParametersRepository;
import com.synthilearn.dictionaryservice.domain.DictionaryParameters;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.DictionaryParametersEntity;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper.DictionaryParametersMapper;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.repository.DictionaryParametersJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DictionaryParametersRepositoryImpl implements DictionaryParametersRepository {

    private final DictionaryParametersJpaRepository dictionaryParametersJpaRepository;
    private final DictionaryParametersMapper dictionaryParametersMapper;

    @Override
    @Transactional
    public Mono<DictionaryParameters> initDictionary(UUID workareaId) {
        return dictionaryParametersJpaRepository.save(DictionaryParametersEntity.builder()
                .id(workareaId)
                .showTranslation(true)
                .newRecord(true)
                .build()
        ).map(dictionaryParametersMapper::map);
    }
}
