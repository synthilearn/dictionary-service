package com.synthilearn.dictionaryservice.app.services.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.synthilearn.dictionaryservice.app.port.PhraseRepository;
import com.synthilearn.dictionaryservice.app.port.PhraseTranslateRepository;
import com.synthilearn.dictionaryservice.app.port.TemplateRepository;
import com.synthilearn.dictionaryservice.app.services.TemplateService;
import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.domain.PhraseStatus;
import com.synthilearn.dictionaryservice.domain.PhraseTranslate;
import com.synthilearn.dictionaryservice.domain.PhraseType;
import com.synthilearn.dictionaryservice.domain.Template;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.TranslationShort;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TemplateRepositoryServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final PhraseRepository phraseRepository;
    private final PhraseTranslateRepository phraseTranslateRepository;

    public Mono<Template> save(Template template) {
        return templateRepository.addTemplate(template.toBuilder()
                .id(UUID.randomUUID())
                .creationDate(ZonedDateTime.now())
                .build());
    }

    @Override
    public Mono<List<Template>> getAll() {
        return templateRepository.getAllTemplates();
    }

    @Override
    public Mono<Void> execute(UUID templateId, UUID dictionaryId) {
        return templateRepository.findTemplateById(templateId)
                .flatMap(template -> {
                    Map<Phrase, Set<PhraseTranslate>> phraseSetMap =
                            parsePhrases(template.getFile());

                    phraseSetMap.entrySet().forEach(entry -> {
                        Phrase phrase = entry.getKey();
                        Set<PhraseTranslate> newTranslates = entry.getValue();
                        phraseRepository.findByText(phrase.getText(), dictionaryId)
                                .singleOptional()
                                .flatMap(phraseOpt -> {
                                    if (phraseOpt.isPresent()) {
                                        Phrase currentPhrase = phraseOpt.get();
                                        phraseTranslateRepository.findByPhraseId(currentPhrase.getId())
                                                .flatMap(existingPhraseTranslates -> {
                                                    newTranslates.forEach(newTranslate -> {
                                                        if (!existingPhraseTranslates.contains(newTranslate)) {
                                                            phraseTranslateRepository.addTranslate(
                                                                    initTranslate(newTranslate, currentPhrase.getId())).subscribe();
                                                        }
                                                    });
                                                    return Mono.empty();
                                                }).subscribe();
                                        return Mono.empty();
                                    }
                                    return phraseRepository.initPhrase(phrase.toBuilder()
                                                    .id(UUID.randomUUID())
                                                    .dictionaryId(dictionaryId)
                                                    .status(PhraseStatus.ACTIVE)
                                                    .build())
                                            .map(phraseEntity -> {
                                                phraseTranslateRepository.updatePhraseTranslations(
                                                        phraseEntity.getId(),
                                                        newTranslates.stream().map(newTranslate ->
                                                                        new TranslationShort(
                                                                                newTranslate.getPartOfSpeech(),
                                                                                newTranslate.getTranslationText()))
                                                                .toList())
                                                        .subscribe();
                                                return Mono.empty();
                                            });
                                }).subscribe();
                    });

                    return Mono.empty();
                });
    }

    private PhraseTranslate initTranslate(PhraseTranslate phraseTranslate, UUID phraseId) {
        return phraseTranslate.toBuilder()
                .learnLevel(30)
                .phraseId(phraseId)
                .creationDate(ZonedDateTime.now())
                .status(PhraseStatus.ACTIVE)
                .id(UUID.randomUUID())
                .build();
    }

    private Map<Phrase, Set<PhraseTranslate>> parsePhrases(byte[] file) {
        Map<Phrase, Set<PhraseTranslate>> phrasesMap = new HashMap<>();
        try (InputStream inputStream = new ByteArrayInputStream(file);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String word = parts[0];
                    PhraseType type = PhraseType.valueOf(parts[1].toUpperCase());
                    PartOfSpeech partOfSpeech = parts[2].isBlank() ? null :
                            PartOfSpeech.valueOf(parts[2].toUpperCase());
                    String[] translations = parts[3].split(", ");
                    phrasesMap.merge(new Phrase(word, type), new HashSet<>() {{
                        Arrays.stream(translations)
                                .forEach(translate -> add(PhraseTranslate.builder()
                                        .translationText(translate.replaceAll("\"", ""))
                                        .partOfSpeech(partOfSpeech)
                                        .build()));
                    }}, (oldSet, newSet) -> {
                        oldSet.addAll(newSet);
                        return oldSet;
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return phrasesMap;
    }
}
