package com.synthilearn.dictionaryservice.infra.persistence.jpa.repository;

import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.PhraseTranslateEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhraseTranslateJpaRepository extends ReactiveCrudRepository<PhraseTranslateEntity, UUID> {
}
