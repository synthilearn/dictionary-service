package com.synthilearn.dictionaryservice.app.services;

import java.util.List;
import java.util.UUID;

import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.domain.Template;

import reactor.core.publisher.Mono;

public interface TemplateService {

    Mono<Template> save(Template template);
    Mono<List<Template>> getAll();
    Mono<Void> execute(UUID templateId, UUID dictionaryId);
    Mono<List<Phrase>> getPreview(UUID templateId);
}
