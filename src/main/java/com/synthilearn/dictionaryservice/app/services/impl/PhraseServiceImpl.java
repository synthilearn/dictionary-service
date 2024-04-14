package com.synthilearn.dictionaryservice.app.services.impl;

import com.synthilearn.dictionaryservice.app.port.PhraseRepository;
import com.synthilearn.dictionaryservice.app.port.PhraseTranslateRepository;
import com.synthilearn.dictionaryservice.app.services.PhraseService;
import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.domain.PhraseStatus;
import com.synthilearn.dictionaryservice.domain.mapper.PhraseDtoMapper;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.InitPhraseRequest;
import com.synthilearn.dictionaryservice.infra.api.rest.exception.PhraseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhraseServiceImpl implements PhraseService {

    private final PhraseRepository phraseRepository;
    private final PhraseTranslateRepository phraseTranslateRepository;
    private final PhraseDtoMapper phraseDtoMapper;

    @Override
    @Transactional
    public Mono<Phrase> initPhrase(InitPhraseRequest request) {
        return phraseRepository.findByTextAndType(request.getText(), request.getPartOfSpeech())
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
    public Mono<Phrase> findByPartAndText(PartOfSpeech part, String text) {
        return phraseRepository.findByTextAndType(text, part)
                .flatMap(phrase -> phraseTranslateRepository.findByPhraseId(phrase.getId())
                        .map(phraseTranslates -> {
                            phrase.setPhraseTranslates(phraseTranslates);
                            return phrase;
                        }))
                .switchIfEmpty(Mono.error(PhraseException.notFoundByPartAndText(part, text)));
    }

    private Phrase initPhraseDomain(InitPhraseRequest request) {
        return phraseDtoMapper.map(request).toBuilder()
                .id(UUID.randomUUID())
                .status(PhraseStatus.ACTIVE)
                .build();
    }
}
