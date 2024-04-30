package com.synthilearn.dictionaryservice.infra.persistence.jpa.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.TemplateEntity;

@Repository
public interface TemplateJpaRepository extends ReactiveCrudRepository<TemplateEntity, UUID> {
}
