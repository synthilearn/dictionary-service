package com.synthilearn.dictionaryservice.infra.api.rest;

import com.synthilearn.commonstarter.GenericResponse;
import com.synthilearn.dictionaryservice.app.services.DictionaryService;
import com.synthilearn.dictionaryservice.domain.Dictionary;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.CreateDictionaryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dictionary-service/v1/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @PostMapping
    public Mono<GenericResponse<Dictionary>> createDictionary(@RequestBody @Valid CreateDictionaryDto createDictionaryDto) {
        return dictionaryService.createDictionary(createDictionaryDto)
                .map(GenericResponse::ok);
    }
}
