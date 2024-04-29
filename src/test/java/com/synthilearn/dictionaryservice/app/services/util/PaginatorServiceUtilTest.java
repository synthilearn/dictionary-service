package com.synthilearn.dictionaryservice.app.services.util;

import static com.synthilearn.dictionaryservice.app.services.util.PaginatorServiceUtil.paginate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import com.synthilearn.dictionaryservice.domain.Groups;
import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.domain.PhraseTranslate;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;

class PaginatorServiceUtilTest {

    private final List<Phrase> phrasesList = new ArrayList<>() {{
        add(Phrase.builder().text("await")
                .phraseTranslates(List.of(
                        PhraseTranslate.builder().partOfSpeech(PartOfSpeech.VERB)
                                .translationText("await1").build(),
                        PhraseTranslate.builder().partOfSpeech(PartOfSpeech.ADJECTIVE)
                                .translationText("await2").build()))
                .build());
        add(Phrase.builder().text("bas")
                .phraseTranslates(List.of(
                        PhraseTranslate.builder().partOfSpeech(PartOfSpeech.NOUN)
                                .translationText("bas1").build(),
                        PhraseTranslate.builder().partOfSpeech(PartOfSpeech.OTHER)
                                .translationText("bas2").build()))
                .build());
        add(Phrase.builder().text("beast")
                .phraseTranslates(List.of(
                        PhraseTranslate.builder().partOfSpeech(PartOfSpeech.NOUN)
                                .translationText("beast1").build()))
                .build());
        add(Phrase.builder().text("beer")
                .phraseTranslates(List.of(
                        PhraseTranslate.builder().partOfSpeech(PartOfSpeech.NOUN)
                                .translationText("beer1").build(),
                        PhraseTranslate.builder().partOfSpeech(PartOfSpeech.VERB)
                                .translationText("beer2").build()))
                .build());
        add(Phrase.builder().text("call")
                .phraseTranslates(List.of(
                        PhraseTranslate.builder().partOfSpeech(PartOfSpeech.NOUN)
                                .translationText("call1").build(),
                        PhraseTranslate.builder().partOfSpeech(PartOfSpeech.VERB)
                                .translationText("call2").build()))
                .build());
    }};

    @Test
    void paginateEmptyGrouping_nullOffset_fullSize() {
        PaginateInfo result =
                paginate(phrasesList, GetAllPhraseRequestDto.builder()
                        .size(10)
                        .groups(new ArrayList<>())
                        .page(0)
                        .build());

        List<Phrase> phrasesResult = (List<Phrase>) result.getPhrases();
        assertEquals(phrasesResult.size(), phrasesList.size());
        assertEquals(result.getTotalPages(), 1);
    }

    @Test
    void paginateEmptyGrouping_nullOffset_limitedSize() {
        PaginateInfo result =
                paginate(phrasesList, GetAllPhraseRequestDto.builder()
                        .size(2)
                        .groups(new ArrayList<>())
                        .page(0)
                        .build());

        List<Phrase> phrasesResult = (List<Phrase>) result.getPhrases();
        assertEquals(2, phrasesResult.size());
        assertEquals(3, result.getTotalPages());
        assertTrue(phrasesResult.stream()
                .allMatch(el -> el.getText().equals("bas") || el.getText().equals("await")));
    }

    @Test
    void paginateEmptyGrouping_limitedOffset() {
        PaginateInfo result =
                paginate(phrasesList, GetAllPhraseRequestDto.builder()
                        .size(2)
                        .groups(new ArrayList<>())
                        .page(1)
                        .build());

        List<Phrase> phrasesResult = (List<Phrase>) result.getPhrases();
        assertEquals(2, phrasesResult.size());
        assertEquals(3, result.getTotalPages());
        assertTrue(phrasesResult.stream()
                .allMatch(el -> el.getText().equals("beast") || el.getText().equals("beer")));
    }

    @Test
    void paginateEmptyGrouping_emptyList() {
        PaginateInfo result =
                paginate(new ArrayList<>(), GetAllPhraseRequestDto.builder()
                        .size(2)
                        .groups(new ArrayList<>())
                        .page(1)
                        .build());

        List<Phrase> phrasesResult = (List<Phrase>) result.getPhrases();
        assertEquals(0, phrasesResult.size());
    }

    @Test
    void paginateEmptyGrouping_unboundedOffset() {
        PaginateInfo result =
                paginate(phrasesList, GetAllPhraseRequestDto.builder()
                        .size(2)
                        .groups(new ArrayList<>())
                        .page(100)
                        .build());

        List<Phrase> phrasesResult = (List<Phrase>) result.getPhrases();
        assertEquals(0, phrasesResult.size());
    }

    @Test
    void paginateOneGrouping_emptyOffset_fullSize() {
        GetAllPhraseRequestDto request = GetAllPhraseRequestDto.builder().size(100)
                .groups(List.of(Groups.LETTER))
                .page(0)
                .build();
        Map<String, TreeSet<Phrase>> groupedPhrases =
                (Map<String, TreeSet<Phrase>>) GroupServiceUtil.groupPhrases(phrasesList, request);

        PaginateInfo result =
                paginate(groupedPhrases, request);

        Map<String, TreeSet<Phrase>> phrasesResult =
                (Map<String, TreeSet<Phrase>>) result.getPhrases();

        assertEquals(8,
                phrasesResult.values().stream().mapToInt(phrases -> phrases.size() + 1).sum());
    }

    @Test
    void paginateOneGrouping_emptyOffset_limitedSize() {
        for (int i = 1; i <= phrasesList.size(); i++) {
            GetAllPhraseRequestDto request = GetAllPhraseRequestDto.builder().size(i)
                    .groups(List.of(Groups.LETTER))
                    .page(0)
                    .build();
            Map<String, TreeSet<Phrase>> groupedPhrases =
                    (Map<String, TreeSet<Phrase>>) GroupServiceUtil.groupPhrases(phrasesList,
                            request);

            PaginateInfo result =
                    paginate(groupedPhrases, request);

            Map<String, TreeSet<Phrase>> phrasesResult =
                    (Map<String, TreeSet<Phrase>>) result.getPhrases();

            assertEquals(i,
                    phrasesResult.values().stream().mapToInt(phrases -> phrases.size() + 1).sum());
        }
    }

    @Test
    void paginateOneGrouping_limitedOffset_limitedSize() {
        int[] sizes = {1, 2, 3, 4, 5, 4};
        int[] offsets = {3, 3, 2, 1, 0, 2};
        int[] totalPages = {8, 4, 3, 2, 2, 2};
        Map<Integer, Predicate<Map<String, TreeSet<Phrase>>>> phrasesResults = new HashMap<>() {{
            put(0, phrases -> phrases.values().stream().flatMap(Collection::stream).toList()
                    .stream().allMatch(x -> x.getText().equals("bas")));
            put(1, phrases -> phrases.values().stream().flatMap(Collection::stream).toList()
                    .stream().allMatch(x -> x.getText().equals("call")));
            put(2, phrases -> phrases.values().stream().flatMap(Collection::stream).toList()
                    .stream().allMatch(x -> x.getText().equals("call")));
            put(3, phrases -> phrases.values().stream().flatMap(Collection::stream).toList()
                    .stream()
                    .allMatch(x -> x.getText().equals("beast") || x.getText().equals("beer")));
            put(4, phrases -> phrases.values().stream().flatMap(Collection::stream).toList()
                    .stream()
                    .allMatch(x -> x.getText().equals("await") ||
                            x.getText().equals("bas") ||
                            x.getText().equals("beast")));
            put(5, phrases -> phrases.values().stream().flatMap(Collection::stream).toList()
                    .stream()
                    .allMatch(x -> x.getText().equals("call") ||
                            x.getText().equals("bas") ||
                            x.getText().equals("beast")));
        }};

        for (int i = 0; i < 5; i++) {
            GetAllPhraseRequestDto request = GetAllPhraseRequestDto.builder().size(sizes[i])
                    .groups(List.of(Groups.LETTER))
                    .page(offsets[i])
                    .build();
            Map<String, TreeSet<Phrase>> groupedPhrases =
                    (Map<String, TreeSet<Phrase>>) GroupServiceUtil.groupPhrases(phrasesList,
                            request);

            PaginateInfo result = paginate(groupedPhrases, request);

            Map<String, TreeSet<Phrase>> phrasesResult =
                    (Map<String, TreeSet<Phrase>>) result.getPhrases();

            assertTrue(sizes[i] >=
                            phrasesResult.values().stream().mapToInt(phrases -> phrases.size() + 1).sum(),
                    "Ошибка на итерации: " + i);
            assertEquals((int) result.getTotalPages(), totalPages[i]);
            assertTrue(phrasesResults.get(i).test(phrasesResult));
        }
    }

    @Test
    void paginateTwoGrouping_emptyOffset_fullSize() {
        GetAllPhraseRequestDto request = GetAllPhraseRequestDto.builder().size(100)
                .groups(List.of(Groups.PART_OF_SPEECH, Groups.LETTER))
                .page(0)
                .build();
        Map<String, Map<String, TreeSet<Phrase>>> groupedPhrases =
                (Map<String, Map<String, TreeSet<Phrase>>>) GroupServiceUtil.groupPhrases(
                        phrasesList, request);

        PaginateInfo result =
                paginate(groupedPhrases, request);

        Map<String, Map<String, TreeSet<Phrase>>> phrasesResult =
                (Map<String, Map<String, TreeSet<Phrase>>>) result.getPhrases();

        assertEquals(19,
                phrasesResult.values().stream().mapToInt(inner -> inner.values().stream().mapToInt(
                        treeSet -> treeSet.size() + 1).sum() + 1).sum());
    }

    @Test
    void paginateTwoGrouping_emptyOffset_limitedSize() {
        for (int i = 2; i <= phrasesList.size(); i++) {
            GetAllPhraseRequestDto request = GetAllPhraseRequestDto.builder().size(i)
                    .groups(List.of(Groups.PART_OF_SPEECH, Groups.LETTER))
                    .page(0)
                    .build();
            Map<String, Map<String, TreeSet<Phrase>>> groupedPhrases =
                    (Map<String, Map<String, TreeSet<Phrase>>>) GroupServiceUtil.groupPhrases(
                            phrasesList, request);

            PaginateInfo result =
                    paginate(groupedPhrases, request);

            Map<String, Map<String, TreeSet<Phrase>>> phrasesResult =
                    (Map<String, Map<String, TreeSet<Phrase>>>) result.getPhrases();

            assertEquals(i,
                    phrasesResult.values().stream()
                            .mapToInt(inner -> inner.values().stream().mapToInt(
                                    treeSet -> treeSet.size() + 1).sum() + 1).sum());
        }
    }

    @Test
    void paginateTwoGrouping_limitedOffset_limitedSize() {
        int[] sizes = {5, 4, 3, 16};
        int[] offsets = {1, 1, 3, 1};
        int[] totalPages = {4, 5, 7, 2};
        Map<Integer, Predicate<Map<String, Map<String, TreeSet<Phrase>>>>> phrasesResults =
                new HashMap<>() {{
                    put(0, phrases -> phrases.values().stream().flatMap(inner -> inner.values()
                                    .stream())
                            .flatMap(Collection::stream).toList()
                            .stream().allMatch(x -> x.getPhraseTranslates().stream().anyMatch(
                                    phraseTranslate -> phraseTranslate.getTranslationText()
                                            .equals("bas1")) ||
                                    x.getPhraseTranslates().stream().anyMatch(
                                            phraseTranslate -> phraseTranslate.getTranslationText()
                                                    .equals("beast1")) ||
                                    x.getPhraseTranslates().stream().anyMatch(
                                            phraseTranslate -> phraseTranslate.getTranslationText()
                                                    .equals("beer1"))));
                    put(1, phrases -> phrases.values().stream().flatMap(inner -> inner.values()
                                    .stream())
                            .flatMap(Collection::stream).toList()
                            .stream().allMatch(x -> x.getPhraseTranslates().stream().anyMatch(
                                    phraseTranslate -> phraseTranslate.getTranslationText()
                                            .equals("await1"))));
                    put(2, phrases -> phrases.values().stream().flatMap(inner -> inner.values()
                                    .stream())
                            .flatMap(Collection::stream).toList()
                            .stream().allMatch(x -> x.getPhraseTranslates().stream().anyMatch(
                                    phraseTranslate -> phraseTranslate.getTranslationText()
                                            .equals("beer1") ||
                                            phraseTranslate.getTranslationText()
                                                    .equals("bas2"))));
                    put(3, phrases -> phrases.values().stream().flatMap(inner -> inner.values()
                                    .stream())
                            .flatMap(Collection::stream).toList()
                            .stream().allMatch(x -> x.getPhraseTranslates().stream().anyMatch(
                                    phraseTranslate -> phraseTranslate.getTranslationText()
                                            .equals("call1") ||
                                            phraseTranslate.getTranslationText()
                                                    .equals("call2"))));
                }};

        for (int i = 0; i < sizes.length; i++) {
            GetAllPhraseRequestDto request = GetAllPhraseRequestDto.builder().size(sizes[i])
                    .groups(List.of(Groups.PART_OF_SPEECH, Groups.LETTER))
                    .page(offsets[i])
                    .build();
            Map<String, Map<String, TreeSet<Phrase>>> groupedPhrases =
                    (Map<String, Map<String, TreeSet<Phrase>>>) GroupServiceUtil.groupPhrases(
                            phrasesList, request);

            PaginateInfo result = paginate(groupedPhrases, request);

            Map<String, Map<String, TreeSet<Phrase>>> phrasesResult =
                    (Map<String, Map<String, TreeSet<Phrase>>>) result.getPhrases();

            assertTrue(sizes[i] >=
                            phrasesResult.values().stream()
                                    .mapToInt(inner -> inner.values().stream().mapToInt(
                                            treeSet -> treeSet.size() + 1).sum() + 1).sum(),
                    "Ошибка на итерации: " + i);
            assertEquals((int) result.getTotalPages(), totalPages[i]);
            assertTrue(phrasesResults.get(i).test(phrasesResult));
        }
    }
}