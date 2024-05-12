package com.synthilearn.dictionaryservice.app.services.impl;

import static com.synthilearn.dictionaryservice.app.services.util.GroupServiceUtil.groupPhrases;
import static com.synthilearn.dictionaryservice.app.services.util.PaginatorServiceUtil.paginate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synthilearn.dictionaryservice.app.port.DictionaryParametersRepository;
import com.synthilearn.dictionaryservice.app.port.PhraseRepository;
import com.synthilearn.dictionaryservice.app.port.PhraseTranslateRepository;
import com.synthilearn.dictionaryservice.app.services.PhraseService;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.domain.PhraseStatus;
import com.synthilearn.dictionaryservice.domain.PhraseTranslate;
import com.synthilearn.dictionaryservice.domain.mapper.PhraseDtoMapper;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.ChangeProgressRequest;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.InitPhraseRequest;
import com.synthilearn.dictionaryservice.infra.api.rest.exception.PhraseException;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.repository.PhraseTranslateJpaRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class PhraseServiceImpl implements PhraseService {

    private final PhraseRepository phraseRepository;
    private final PhraseTranslateRepository phraseTranslateRepository;
    private final DictionaryParametersRepository dictionaryParametersRepository;
    private final PhraseDtoMapper phraseDtoMapper;
    private final PhraseTranslateJpaRepository phraseTranslateJpaRepository;

    @Override
    public Mono<Phrase> initPhrase(InitPhraseRequest request) {
        return phraseRepository.findById(request.getId())
                .singleOptional()
                .flatMap(phraseOpt -> phraseOpt.<Mono<? extends Phrase>>map(phrase -> {
                            phrase.setText(request.getText());
                            return phraseRepository.save(phrase);
                        })
                        .orElse(phraseRepository.initPhrase(initPhraseDomain(request))))
                .flatMap(phrase -> phraseTranslateRepository
                        .updatePhraseTranslations(phrase.getId(), request.getPhraseTranslates())
                        .map(phraseTranslates -> {
                            phrase.setPhraseTranslates(phraseTranslates);
                            return phraseTranslates;
                        })
                        .thenReturn(phrase));
    }

    @Override
    public Mono<Phrase> findByDictionaryAndText(UUID dictionaryId, String text) {
        return phraseRepository.findByText(text, dictionaryId)
                .flatMap(phrase -> phraseTranslateRepository.findByPhraseId(phrase.getId())
                        .map(phraseTranslates -> {
                            phrase.setPhraseTranslates(phraseTranslates);
                            return phrase;
                        }))
                .switchIfEmpty(
                        Mono.error(PhraseException.notFoundByPartAndText(dictionaryId, text)));
    }

    @Override
    public Mono<Phrase> findById(UUID phraseId) {
        return phraseRepository.findById(phraseId)
                .switchIfEmpty(Mono.error(PhraseException.notFound(phraseId)))
                .flatMap(phrase -> phraseTranslateRepository.findByPhraseId(phrase.getId())
                        .map(phraseTranslates -> {
                            phrase.setPhraseTranslates(phraseTranslates);
                            return phrase;
                        }));
    }

    @Override
    @Transactional
    public Mono<Void> delete(UUID phraseId) {
        return phraseRepository.findById(phraseId)
                .switchIfEmpty(Mono.error(PhraseException.notFound(phraseId)))
                .flatMap(phrase -> {
                    phraseTranslateRepository.deleteAll(phraseId)
                            .subscribe();
                    phraseRepository.delete(phraseId)
                            .subscribe();
                    return Mono.empty();
                });
    }

    @Override
    public Mono<List<Phrase>> findByPart(String textPart, UUID dictionaryId) {
        return phraseRepository.findByTextPart(dictionaryId, textPart)
                .flatMap(phrases -> {
                    List<Mono<Phrase>> monoList = new ArrayList<>();
                    for (Phrase phrase : phrases) {
                        Mono<Phrase> phraseWithTranslates = phraseTranslateRepository
                                .findByPhraseId(phrase.getId())
                                .map(phraseTranslates -> {
                                    phrase.setPhraseTranslates(phraseTranslates);
                                    return phrase;
                                });
                        monoList.add(phraseWithTranslates);
                    }
                    return Mono.zip(monoList, objects -> phrases);
                });
    }

    @Override
    @Transactional
    public Mono<Void> changeProgress(ChangeProgressRequest request) {
        return Flux.fromIterable(request.getProgressEntries())
                .flatMap(translate -> phraseTranslateJpaRepository.findById(translate.getId())
                        .flatMap(translateEntity -> {
                            translateEntity.setLearnLevel(translate.getNewProgress());
                            return phraseTranslateJpaRepository.save(translateEntity);
                        })
                        .subscribeOn(Schedulers.parallel()))
                .then();
    }

    @Override
    @Transactional
    public Mono<Object> findAll(GetAllPhraseRequestDto requestDto) {
        return phraseRepository.findAll(requestDto)
                .flatMap(phrases -> {
                    dictionaryParametersRepository.editParameters(requestDto)
                            .subscribe();
                    List<Mono<Phrase>> monoList = new ArrayList<>();
                    for (Phrase phrase : phrases) {
                        Mono<Phrase> phraseWithTranslates = phraseTranslateRepository
                                .findByPhraseId(phrase.getId())
                                .map(phraseTranslates -> {
                                    List<PhraseTranslate> filteredPhraseTranslates =
                                            phraseTranslates.stream()
                                                    .filter(phraseTranslate -> requestDto.getPartsOfSpeech()
                                                            .contains(
                                                                    phraseTranslate.getPartOfSpeech()))
                                                    .collect(Collectors.toList());
                                    phrase.setPhraseTranslates(filteredPhraseTranslates);
                                    return phrase;
                                });
                        monoList.add(phraseWithTranslates);
                    }
                    return Mono.zip(monoList, objects -> phrases);
                })
                .flatMap(phrases -> Mono.just(groupPhrases(phrases, requestDto)))
                .flatMap(phrases -> Mono.just(paginate(phrases, requestDto)));
    }

    private Phrase initPhraseDomain(InitPhraseRequest request) {
        return phraseDtoMapper.map(request).toBuilder()
                .id(UUID.randomUUID())
                .status(PhraseStatus.ACTIVE)
                .build();
    }
}
