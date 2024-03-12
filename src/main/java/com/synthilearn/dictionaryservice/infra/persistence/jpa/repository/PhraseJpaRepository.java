package com.synthilearn.dictionaryservice.infra.persistence.jpa.repository;

import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.PhraseEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhraseJpaRepository extends ReactiveCrudRepository<PhraseEntity, UUID> {
}
