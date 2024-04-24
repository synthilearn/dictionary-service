package com.synthilearn.dictionaryservice.app.port.impl;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synthilearn.dictionaryservice.app.port.DictionaryParametersRepository;
import com.synthilearn.dictionaryservice.domain.DictionaryParameters;
import com.synthilearn.dictionaryservice.domain.Groups;
import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.PhraseType;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.DictionaryParametersEntity;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper.DictionaryParametersMapper;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.repository.DictionaryParametersJpaRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

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

    @Override
    @Transactional
    public Mono<DictionaryParameters> editParameters(GetAllPhraseRequestDto requestDto) {
        LocalDate startDate = LocalDate.ofYearDay(1900, 1);
        LocalDate endDate = LocalDate.ofYearDay(2100, 1);
        return dictionaryParametersJpaRepository.findById(requestDto.getDictionaryId())
                .map(dictionaryParametersEntity -> dictionaryParametersEntity.toBuilder()
                        .showTranslation(requestDto.getShowTranslates())
                        .dateFrom(requestDto.getStartDate().isEqual(startDate) ? startDate :
                                requestDto.getStartDate())
                        .dateTo(requestDto.getEndDate().isEqual(endDate) ? endDate :
                                requestDto.getEndDate())
                        .partsOfSpeech(
                                requestDto.getPartsOfSpeech().stream().map(PartOfSpeech::name)
                                        .collect(Collectors.toSet()).toString())
                        .groups(requestDto.getGroups().stream().map(Groups::name)
                                .collect(Collectors.toSet()).toString())
                        .phraseTypes(
                                requestDto.getPhraseTypes().stream().map(PhraseType::name)
                                        .collect(Collectors.toSet()).toString())
                        .build())
                .flatMap(dictionaryParametersJpaRepository::save)
                .map(dictionaryParametersMapper::map);
    }

    @Override
    public Mono<DictionaryParameters> findByDictionaryId(UUID dictionaryId) {
        return dictionaryParametersJpaRepository.findById(dictionaryId)
                .map(dictionaryParametersMapper::map);
    }
}
