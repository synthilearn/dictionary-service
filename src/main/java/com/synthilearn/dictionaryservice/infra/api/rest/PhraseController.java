package com.synthilearn.dictionaryservice.infra.api.rest;


import com.synthilearn.commonstarter.GenericResponse;
import com.synthilearn.dictionaryservice.app.services.PhraseService;
import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.InitPhraseRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dictionary-service/v1/phrase")
public class PhraseController {

    private final PhraseService phraseService;

    @PostMapping
    public Mono<GenericResponse<Phrase>> addPhrase(@RequestBody @Valid InitPhraseRequest initPhraseRequest) {
        return phraseService.initPhrase(initPhraseRequest)
                .map(GenericResponse::ok);
    }

    @GetMapping("/{partOfSpeech}/{text}")
    public Mono<GenericResponse<Phrase>> findPhraseByPartOfSpeechAndText(
            @PathVariable PartOfSpeech partOfSpeech, @PathVariable String text) {
        return phraseService.findByPartAndText(partOfSpeech, text)
                .map(GenericResponse::ok);
    }
}
