package com.synthilearn.dictionaryservice.infra.api.rest;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synthilearn.commonstarter.GenericResponse;
import com.synthilearn.dictionaryservice.app.services.DictionaryParametersService;
import com.synthilearn.dictionaryservice.domain.DictionaryParameters;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dictionary-service/v1/dictionary-parameters")
public class DictionaryParametersController {

    private final DictionaryParametersService dictionaryParametersService;

    @GetMapping("/{dictionaryId}")
    public Mono<GenericResponse<DictionaryParameters>> getParameters(
            @PathVariable UUID dictionaryId) {
        return dictionaryParametersService.findByDictionary(dictionaryId)
                .map(GenericResponse::ok);
    }
}
