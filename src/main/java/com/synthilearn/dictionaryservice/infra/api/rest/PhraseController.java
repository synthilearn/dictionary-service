package com.synthilearn.dictionaryservice.infra.api.rest;


import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synthilearn.commonstarter.GenericResponse;
import com.synthilearn.dictionaryservice.app.services.PhraseService;
import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.InitPhraseRequest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dictionary-service/v1/phrase")
public class PhraseController {

    private final PhraseService phraseService;

    @PostMapping
    public Mono<GenericResponse<Phrase>> addPhrase(
            @RequestBody @Valid InitPhraseRequest initPhraseRequest) {
        return phraseService.initPhrase(initPhraseRequest)
                .map(GenericResponse::ok);
    }

    @GetMapping("/{partOfSpeech}/{text}")
    public Mono<GenericResponse<Phrase>> findPhraseByPartOfSpeechAndText(
            @PathVariable PartOfSpeech partOfSpeech, @PathVariable String text) {
        return phraseService.findByPartAndText(partOfSpeech, text)
                .map(GenericResponse::ok);
    }

    @PostMapping("/all")
    public Mono<GenericResponse<Object>> getAll(
            @RequestBody @Valid GetAllPhraseRequestDto requestDto) {
        return phraseService.findAll(requestDto)
                .map(GenericResponse::ok);
    }
}
