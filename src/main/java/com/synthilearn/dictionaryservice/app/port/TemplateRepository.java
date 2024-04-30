package com.synthilearn.dictionaryservice.app.port;

import java.util.List;
import java.util.UUID;

import com.synthilearn.dictionaryservice.domain.Template;

import reactor.core.publisher.Mono;

public interface TemplateRepository {
    Mono<Template> addTemplate(Template template);
    Mono<List<Template>> getAllTemplates();
    Mono<Template> findTemplateById(UUID templateId);
}
