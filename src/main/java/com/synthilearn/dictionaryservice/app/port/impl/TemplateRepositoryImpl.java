package com.synthilearn.dictionaryservice.app.port.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synthilearn.dictionaryservice.app.port.TemplateRepository;
import com.synthilearn.dictionaryservice.domain.Template;
import com.synthilearn.dictionaryservice.infra.api.rest.exception.parent.GenericException;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper.TemplateMapper;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.repository.TemplateJpaRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TemplateRepositoryImpl implements TemplateRepository {

    private final TemplateMapper templateMapper;
    private final TemplateJpaRepository repository;

    @Transactional
    public Mono<Template> addTemplate(Template template) {
        return repository.save(templateMapper.map(template).toBuilder().newRecord(true).build())
                .map(templateMapper::map);
    }

    @Override
    public Mono<List<Template>> getAllTemplates() {
        return repository.findAll().map(templateMapper::map)
                .collectList();
    }

    @Override
    public Mono<Template> findTemplateById(UUID templateId) {
        return repository.findById(templateId)
                .map(templateMapper::map)
                .switchIfEmpty(Mono.error(
                        new GenericException("Шаблон не найден", HttpStatus.NOT_FOUND, 1000)));
    }
}
