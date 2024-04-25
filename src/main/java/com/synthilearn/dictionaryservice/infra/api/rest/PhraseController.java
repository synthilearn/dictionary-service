package com.synthilearn.dictionaryservice.infra.api.rest;


import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synthilearn.commonstarter.GenericResponse;
import com.synthilearn.dictionaryservice.app.services.PhraseService;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.InitPhraseRequest;

import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{dictionaryId}/{text}")
    public Mono<GenericResponse<Phrase>> findPhraseByPartOfSpeechAndText(
            @PathVariable UUID dictionaryId, @PathVariable String text) {
        return phraseService.findByDictionaryAndText(dictionaryId, text)
                .map(GenericResponse::ok);
    }

    @GetMapping("/{phraseId}")
    public Mono<GenericResponse<Phrase>> findById(
            @PathVariable UUID phraseId) {
        return phraseService.findById(phraseId)
                .map(GenericResponse::ok);
    }

    @DeleteMapping("/{phraseId}")
    public Mono<GenericResponse<Void>> deletePhrase(
            @PathVariable UUID phraseId) {
        return phraseService.delete(phraseId)
                .map(GenericResponse::ok);
    }

    @PostMapping("/all")
    public Mono<GenericResponse<Object>> getAll(
            @RequestBody @Valid GetAllPhraseRequestDto requestDto) {
        return phraseService.findAll(requestDto)
                .map(GenericResponse::ok);
    }

    @GetMapping("/by-part/{textPart}")
    public Mono<GenericResponse<List<Phrase>>> getByPart(@PathVariable String textPart,
                                                         @RequestParam("dictionaryId")
                                                         UUID dictionaryId) {
        return phraseService.findByPart(textPart, dictionaryId)
                .map(GenericResponse::ok);
    }
}
