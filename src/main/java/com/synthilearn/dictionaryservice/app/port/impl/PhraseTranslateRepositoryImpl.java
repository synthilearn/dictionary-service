package com.synthilearn.dictionaryservice.app.port.impl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synthilearn.dictionaryservice.app.port.PhraseRepository;
import com.synthilearn.dictionaryservice.app.port.PhraseTranslateRepository;
import com.synthilearn.dictionaryservice.domain.PhraseStatus;
import com.synthilearn.dictionaryservice.domain.PhraseTranslate;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.TranslationShort;
import com.synthilearn.dictionaryservice.infra.api.rest.exception.PhraseException;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.PhraseTranslateEntity;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper.PhraseTranslateEntityMapper;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.repository.PhraseTranslateJpaRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhraseTranslateRepositoryImpl implements PhraseTranslateRepository {

    private final PhraseTranslateJpaRepository translateJpaRepository;
    private final PhraseRepository phraseRepository;
    private final PhraseTranslateEntityMapper phraseTranslateEntityMapper;

    @Override
    @Transactional
    public Mono<List<PhraseTranslate>> updatePhraseTranslations(UUID phraseId,
                                                                List<TranslationShort> translations) {
        return phraseRepository.findById(phraseId)
                .switchIfEmpty(Mono.error(PhraseException.notFound(phraseId)))
                .flatMap(phrase -> {
                    List<PhraseTranslateEntity> newPhraseTranslateEntities =
                            initListPhraseTranslateEntity(translations, phraseId);

                    return translateJpaRepository.findByPhraseId(phraseId)
                            .collectList()
                            .flatMap(oldTranslates -> {
                                List<TranslationShort> existingTranslations = oldTranslates.stream()
                                        .map(translate -> new TranslationShort(
                                                translate.getPartOfSpeech(),
                                                translate.getTranslationText()))
                                        .toList();

                                List<PhraseTranslateEntity> translationsToRemove =
                                        oldTranslates.stream()
                                                .filter(translate -> translations.stream()
                                                        .noneMatch(x -> x.getTranslationText()
                                                                .equals(translate.getTranslationText())
                                                                && x.getPartOfSpeech()
                                                                .equals(translate.getPartOfSpeech())))
                                                .toList();

                                List<PhraseTranslateEntity> translationsToAdd =
                                        newPhraseTranslateEntities.stream()
                                                .filter(translate -> existingTranslations.stream()
                                                        .noneMatch(existingTranslation ->
                                                                existingTranslation.getTranslationText()
                                                                        .equals(translate.getTranslationText()) &&
                                                                        existingTranslation.getPartOfSpeech()
                                                                                .equals(translate.getPartOfSpeech())))
                                                .toList();

                                translateJpaRepository.deleteAll(translationsToRemove)
                                        .subscribe();

                                translateJpaRepository.saveAll(translationsToAdd)
                                        .subscribe();

                                return Mono.just(newPhraseTranslateEntities.stream()
                                        .map(phraseTranslateEntityMapper::map)
                                        .toList());
                            });
                });
    }

    @Override
    public Mono<List<PhraseTranslate>> findByPhraseId(UUID phraseId) {
        return translateJpaRepository.findByPhraseId(phraseId)
                .map(phraseTranslateEntityMapper::map)
                .collectList();
    }

    @Override
    @Transactional
    public Mono<Void> deleteAll(UUID phraseId) {
        return translateJpaRepository.findByPhraseId(phraseId)
                .flatMap(phraseTranslateEntity -> {
                    assert phraseTranslateEntity.getId() != null;
                    return translateJpaRepository.deleteById(phraseTranslateEntity.getId());
                })
                .then();
    }

    @Override
    @Transactional
    public Mono<PhraseTranslate> addTranslate(PhraseTranslate phraseTranslate) {
        return translateJpaRepository.save(
                phraseTranslateEntityMapper.map(phraseTranslate).toBuilder()
                        .newRecord(true)
                        .build())
                .map(phraseTranslateEntityMapper::map);
    }


    private List<PhraseTranslateEntity> initListPhraseTranslateEntity(
            List<TranslationShort> translations,
            UUID phraseId) {
        return translations.stream().map(translate -> PhraseTranslateEntity.builder()
                .id(UUID.randomUUID())
                .learnLevel(30)
                .phraseId(phraseId)
                .translationText(translate.getTranslationText())
                .partOfSpeech(translate.getPartOfSpeech())
                .creationDate(ZonedDateTime.now())
                .updatedDate(ZonedDateTime.now())
                .status(PhraseStatus.ACTIVE)
                .newRecord(true)
                .build()).toList();
    }
}
