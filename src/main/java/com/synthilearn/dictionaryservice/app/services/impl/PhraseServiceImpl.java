package com.synthilearn.dictionaryservice.app.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synthilearn.dictionaryservice.app.port.PhraseRepository;
import com.synthilearn.dictionaryservice.app.port.PhraseTranslateRepository;
import com.synthilearn.dictionaryservice.app.services.PhraseService;
import com.synthilearn.dictionaryservice.domain.Groups;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.domain.PhraseStatus;
import com.synthilearn.dictionaryservice.domain.PhraseTranslate;
import com.synthilearn.dictionaryservice.domain.mapper.PhraseDtoMapper;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.AllPhrasesDto;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.InitPhraseRequest;
import com.synthilearn.dictionaryservice.infra.api.rest.exception.PhraseException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PhraseServiceImpl implements PhraseService {

    private final PhraseRepository phraseRepository;
    private final PhraseTranslateRepository phraseTranslateRepository;
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
    public Mono<Object> findAll(GetAllPhraseRequestDto requestDto) {
        return phraseRepository.findAll(requestDto)
                .flatMap(phrases -> {
                    if (Boolean.FALSE.equals(requestDto.getShowTranslates())) {
                        return Mono.just(phrases);
                    }
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
                });
//                .flatMap(phrases -> {
//                    if (CollectionUtils.isEmpty(requestDto.getGroups())) {
//                        return Mono.just(resizeEmptyGroup(phrases, requestDto));
//                    }
//                    return Mono.just(group(phrases, requestDto.getGroups(), requestDto));
//                });
    }

    private AllPhrasesDto<List<Phrase>> resizeEmptyGroup(List<Phrase> phrases,
                                                         GetAllPhraseRequestDto requestDto) {
        int totalPages = (int) Math.ceil((double) phrases.size() / requestDto.getSize());
        return new AllPhrasesDto<>(totalPages, phrases.stream()
                .skip((long) requestDto.getSize() * requestDto.getPage())
                .limit(requestDto.getSize())
                .toList());
    }

    private AllPhrasesDto<?> group(List<Phrase> phrases, List<Groups> groups,
                                   GetAllPhraseRequestDto requestDto) {
        TreeMap<String, TreeMap<String, TreeSet<Phrase>>> externalMap =
                new TreeMap<>(Comparator.naturalOrder());

        for (Phrase phrase : phrases) {
            MapKeys keys = getKeys(phrase, groups);

            TreeMap<String, TreeSet<Phrase>> internalMap = new TreeMap<>(Comparator.naturalOrder());
            internalMap.put(keys.getInternalKey(), new TreeSet<>() {{
                add(phrase);
            }});

            externalMap.merge(keys.getExternalKey(),
                    internalMap,
                    (oldValue, newValue) -> {
                        oldValue.merge(keys.getInternalKey(),
                                new TreeSet<>(Comparator.comparing(Phrase::getText)) {{
                                    add(phrase);
                                }}, (oldValueSet, newValueSet) -> {
                                    oldValueSet.addAll(newValueSet);
                                    return oldValueSet;
                                });
                        return oldValue;
                    });
        }

        if (groups.size() == 1) {
            TreeMap<String, TreeSet<Phrase>> combinedMap = new TreeMap<>(Comparator.naturalOrder());

            for (Map.Entry<String, TreeMap<String, TreeSet<Phrase>>> entry : externalMap.entrySet()) {
                TreeSet<Phrase> combinedPhrases = new TreeSet<>(Comparator.naturalOrder());
                for (TreeSet<Phrase> phrases2 : entry.getValue().values()) {
                    combinedPhrases.addAll(phrases2);
                }
                combinedMap.put(entry.getKey(), combinedPhrases);
            }

            return resizeInternalMap(combinedMap, requestDto);
        }

        return new AllPhrasesDto<>(0, externalMap);
    }

    private AllPhrasesDto<TreeMap<String, TreeSet<Phrase>>> resizeInternalMap(
            TreeMap<String, TreeSet<Phrase>> internalMap,
            GetAllPhraseRequestDto requestDto) {

        TreeMap<String, TreeSet<Phrase>> newInternalMap = new TreeMap<>(Comparator.naturalOrder());

        int counter = 0;
        int offset = requestDto.getPage() * requestDto.getSize();
        int lastPosition = offset + requestDto.getSize();

        for (Map.Entry<String, TreeSet<Phrase>> entry : internalMap.entrySet()) {
            counter++;

            int sizeValue = entry.getValue().size();
            if (counter >= offset && counter <= lastPosition) {
                if (counter + sizeValue > lastPosition) {
                    TreeSet<Phrase> combinedPhrases = new TreeSet<>(Comparator.naturalOrder());
                    for (Phrase phrase : entry.getValue()) {
                        if (counter < lastPosition) {
                            combinedPhrases.add(phrase);
                        } else {
                            break;
                        }
                        counter++;
                    }
                    newInternalMap.put(entry.getKey(), combinedPhrases);
                    break;
                }
                newInternalMap.put(entry.getKey(), entry.getValue());
            } else if (counter + sizeValue >= offset && counter + sizeValue <= lastPosition) {
                TreeSet<Phrase> combinedPhrases = new TreeSet<>(Comparator.naturalOrder());
                for (Phrase phrase : entry.getValue()) {
                    if (counter >= offset) {
                        combinedPhrases.add(phrase);
                    }
                    counter++;
                }
                newInternalMap.put(entry.getKey(), combinedPhrases);
                continue;
            }
            counter += sizeValue;
        }


        int amountOfElements = internalMap.size() + internalMap.values().stream()
                .mapToInt(TreeSet::size)
                .sum();

        return new AllPhrasesDto(
                (int) Math.ceil((double) amountOfElements / requestDto.getSize()),
                newInternalMap);
    }

    private MapKeys getKeys(Phrase phrase, List<Groups> groups) {
//        if (groups.getFirst().equals(Groups.LETTER)) {
//            return new MapKeys(phrase.getText().substring(0, 1).toUpperCase(),
//                    phrase.getPartOfSpeech().toString());
//        }
//        return new MapKeys(phrase.getPartOfSpeech().toString(),
//                phrase.getText().substring(0, 1).toUpperCase());
        return null;
    }

    @Getter
    @AllArgsConstructor
    static class MapKeys {
        private String externalKey;
        private String internalKey;
    }

    private Phrase initPhraseDomain(InitPhraseRequest request) {
        return phraseDtoMapper.map(request).toBuilder()
                .id(UUID.randomUUID())
                .status(PhraseStatus.ACTIVE)
                .build();
    }
}
