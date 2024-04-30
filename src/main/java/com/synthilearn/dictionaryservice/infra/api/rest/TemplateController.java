package com.synthilearn.dictionaryservice.infra.api.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.synthilearn.commonstarter.GenericResponse;
import com.synthilearn.dictionaryservice.app.services.TemplateService;
import com.synthilearn.dictionaryservice.domain.Template;
import com.synthilearn.dictionaryservice.domain.mapper.TemplateDtoMapper;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.CreateTemplateRequest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dictionary-service/v1/template")
public class TemplateController {

    private final TemplateService templateService;
    private final TemplateDtoMapper mapper;

    @PostMapping(consumes = "multipart/form-data")
    public Mono<GenericResponse<Template>> addTemplate(@RequestPart(value = "name") String name,
                                                       @RequestPart(value = "file") byte[] file,
                                                       @RequestPart(value = "amountWords")
                                                       String amountWords) {
        return templateService.save(
                        mapper.map(new CreateTemplateRequest(name), file, Integer.valueOf(amountWords)))
                .map(GenericResponse::ok);
    }

    @GetMapping("/all")
    public Mono<GenericResponse<List<Template>>> getAll() {
        return templateService.getAll()
                .map(GenericResponse::ok);
    }

    @PostMapping("/execute/{id}")
    public Mono<Void> execute(@PathVariable UUID id,
                              @RequestParam("dictionaryId") UUID dictionaryId) {
        return templateService.execute(id, dictionaryId);
    }
}
