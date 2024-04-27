package com.synthilearn.dictionaryservice.app.port.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synthilearn.dictionaryservice.app.port.PhraseRepository;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.domain.PhraseStatus;
import com.synthilearn.dictionaryservice.domain.PhraseType;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.PhraseEntity;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper.PhraseEntityMapper;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.repository.PhraseJpaRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhraseRepositoryImpl implements PhraseRepository {

    private final PhraseJpaRepository phraseJpaRepository;
    private final PhraseEntityMapper phraseEntityMapper;

    @Override
    @Transactional
    public Mono<Phrase> initPhrase(Phrase phrase) {
        return phraseJpaRepository.save(initPhraseEntity(phrase))
                .map(phraseEntityMapper::map);
    }

    @Override
    public Mono<Phrase> findById(UUID phraseId) {
        if (phraseId == null) {
            return Mono.empty();
        }
        return phraseJpaRepository.findById(phraseId)
                .map(phraseEntityMapper::map);
    }

    @Override
    public Mono<Phrase> findByText(String text, UUID dictionaryId) {
        return phraseJpaRepository.findFirstByTextAndDictionaryId(text, dictionaryId)
                .map(x -> {
                    System.out.println(x);
                    return phraseEntityMapper.map(x);
                });
    }

    @Override
    public Mono<List<Phrase>> findAll(GetAllPhraseRequestDto requestDto) {
        return phraseJpaRepository.findAllPhrasesWithTranslations(requestDto.getDictionaryId(),
                        requestDto.getPartsOfSpeech(),
                        requestDto.getPhraseTypes().stream().map(PhraseType::name).collect(
                                Collectors.toSet()),
                        ZonedDateTime.of(requestDto.getDateFrom().atStartOfDay().minusSeconds(1),
                                ZoneId.systemDefault()),
                        ZonedDateTime.of(requestDto.getDateTo().atStartOfDay().plusDays(1),
                                ZoneId.systemDefault()))
                .collectList();
    }

    @Override
    @Transactional
    public Mono<Void> delete(UUID phraseId) {
        return phraseJpaRepository.deleteById(phraseId);
    }

    @Override
    public Mono<List<Phrase>> findByTextPart(UUID dictionaryId, String textPart) {
        return phraseJpaRepository.findByTextPart(dictionaryId, textPart)
                .collectList();
    }

    private PhraseEntity initPhraseEntity(Phrase phrase) {
        return phraseEntityMapper.map(phrase).toBuilder()
                .creationDate(ZonedDateTime.now())
                .updatedDate(ZonedDateTime.now())
                .newRecord(true)
                .status(PhraseStatus.ACTIVE)
                .build();
    }

}
