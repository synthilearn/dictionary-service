package com.synthilearn.dictionaryservice.app.services.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.domain.PhraseTranslate;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class GroupPaginatorService {

    public static TreeMap<String, ?> groupPhrases(
            List<Phrase> phrases, GetAllPhraseRequestDto requestDto) {
        TreeMap<String, TreeMap<String, TreeSet<Phrase>>> stringTreeMapTreeMap =
                switch (requestDto.getGroups().getFirst()) {
                    case LETTER -> groupPhrasesByFirstLetter(phrases);
                    case PART_OF_SPEECH -> groupPhrasesByPartOfSpeech(phrases);
                };
        if (Boolean.FALSE.equals(requestDto.getShowTranslation())) {
            stringTreeMapTreeMap.values().stream()
                    .flatMap(x -> Stream.of(x.values()))
                    .flatMap(Collection::stream)
                    .flatMap(Collection::stream)
                    .forEach(phrase -> phrase.setPhraseTranslates(null));
        }
        if (requestDto.getGroups().size() == 1) {
            return collapseToSingleMap(stringTreeMapTreeMap);
        }
        return stringTreeMapTreeMap;
    }

    private static TreeMap<String, TreeSet<Phrase>> collapseToSingleMap(
            TreeMap<String, TreeMap<String, TreeSet<Phrase>>> groupedPhrases) {
        TreeMap<String, TreeSet<Phrase>> collapsedMap = new TreeMap<>();

        groupedPhrases.forEach((key, innerMap) ->
                innerMap.forEach((innerKey, phrasesSet) ->
                        collapsedMap.computeIfAbsent(innerKey, k -> new TreeSet<>())
                                .addAll(phrasesSet)));

        return collapsedMap;
    }


    private static TreeMap<String, TreeMap<String, TreeSet<Phrase>>> groupPhrasesByPartOfSpeech(
            List<Phrase> phrases) {
        TreeMap<String, TreeMap<String, TreeSet<Phrase>>> groupedPhrases = new TreeMap<>();

        Map<String, List<Phrase>> phrasesByFirstLetter = phrases.stream()
                .collect(Collectors.groupingBy(
                        phrase -> phrase.getText().substring(0, 1).toUpperCase()));

        phrasesByFirstLetter.forEach((firstLetter, phrasesList) -> {
            TreeMap<String, TreeSet<Phrase>> innerMap = new TreeMap<>();

            phrasesList.forEach(phrase -> phrase.getPhraseTranslates().forEach(
                    translate -> innerMap.computeIfAbsent(translate.getPartOfSpeech().toString(),
                                    k -> new TreeSet<>())
                            .add(phrase.toBuilder().build())));

            innerMap.forEach((partOfSpeech, phrasesSet) -> phrases.forEach(
                    phrase -> phrase.setPhraseTranslates(phrase.getPhraseTranslates().stream()
                            .filter(phraseTranslate -> phraseTranslate.getPartOfSpeech().name()
                                    .equals(partOfSpeech)).collect(Collectors.toList()))));

            groupedPhrases.put(firstLetter, innerMap);
        });

        return groupedPhrases;
    }

    private static TreeMap<String, TreeMap<String, TreeSet<Phrase>>> groupPhrasesByFirstLetter(
            List<Phrase> phrases) {
        TreeMap<String, TreeMap<String, TreeSet<Phrase>>> groupedPhrases = new TreeMap<>();

        Map<String, List<Phrase>> phrasesByPartOfSpeech = phrases.stream()
                .flatMap(phrase -> phrase.getPhraseTranslates().stream())
                .collect(Collectors.groupingBy(translate -> translate.getPartOfSpeech().toString(),
                        Collectors.mapping(PhraseTranslate::getPhraseId,
                                Collectors.collectingAndThen(Collectors.toList(), phraseIds ->
                                        phrases.stream().filter(phrase ->
                                                        phraseIds.contains(phrase.getId()))
                                                .map(phrase -> phrase.toBuilder().build())
                                                .collect(Collectors.toList())
                                ))));

        phrasesByPartOfSpeech.forEach((partOfSpeech, phrasesList) -> {
            TreeMap<String, TreeSet<Phrase>> innerMap = new TreeMap<>();

            phrasesList.forEach(phrase -> innerMap.computeIfAbsent(
                            phrase.getText().substring(0, 1).toUpperCase(),
                            k -> new TreeSet<>())
                    .add(phrase.toBuilder().build()));

            groupedPhrases.put(partOfSpeech, innerMap);
        });

        groupedPhrases.forEach(
                (speech, phrasesMap) -> phrasesMap.forEach(
                        (firstLetter, phrasesSet) -> phrasesSet.forEach(
                                phrase -> phrase.setPhraseTranslates(
                                        phrase.getPhraseTranslates().stream()
                                                .filter(phraseTranslate -> phraseTranslate.getPartOfSpeech()
                                                        .name().equals(speech)).toList()))));

        return groupedPhrases;
    }
}