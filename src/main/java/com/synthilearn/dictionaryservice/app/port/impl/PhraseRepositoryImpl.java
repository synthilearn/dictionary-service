package com.synthilearn.dictionaryservice.app.port.impl;

import com.synthilearn.dictionaryservice.app.port.PhraseRepository;
import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.domain.PhraseStatus;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.PhraseEntity;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper.PhraseEntityMapper;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.repository.PhraseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.UUID;

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
        return phraseJpaRepository.findById(phraseId)
                .map(phraseEntityMapper::map);
    }

    @Override
    public Mono<Phrase> findByTextAndType(String text, PartOfSpeech part) {
        return phraseJpaRepository.findByTextAndPartOfSpeech(text, part)
                .map(phraseEntityMapper::map);
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
