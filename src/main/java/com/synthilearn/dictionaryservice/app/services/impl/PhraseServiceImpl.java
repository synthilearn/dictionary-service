package com.synthilearn.dictionaryservice.app.services.impl;

import static com.synthilearn.dictionaryservice.app.services.util.GroupPaginatorService.groupPhrases;

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
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.InitPhraseRequest;
import com.synthilearn.dictionaryservice.infra.api.rest.exception.PhraseException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PhraseServiceImpl implements PhraseService {

    private final PhraseRepository phraseRepository;
    private final PhraseTranslateRepository phraseTranslateRepository;
    private final DictionaryParametersRepository dictionaryParametersRepository;
    private final PhraseDtoMapper phraseDtoMapper;

    @Override
    public Mono<Phrase> initPhrase(InitPhraseRequest request) {
        return phraseRepository.findByText(request.getText(), request.getDictionaryId())
                .singleOptional()
                .flatMap(phraseOpt -> phraseOpt.<Mono<? extends Phrase>>map(Mono::just)
                        .orElse(phraseRepository.initPhrase(initPhraseDomain(request))))
                .flatMap(phrase -> phraseTranslateRepository
                        .updatePhraseTranslations(phrase.getId(), request.getTranslations())
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
                .flatMap(phrases -> Mono.just(groupPhrases(phrases, requestDto)));
    }

    private Phrase initPhraseDomain(InitPhraseRequest request) {
        return phraseDtoMapper.map(request).toBuilder()
                .id(UUID.randomUUID())
                .status(PhraseStatus.ACTIVE)
                .build();
    }
}
