package com.synthilearn.dictionaryservice.infra.persistence.jpa.repository;

import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.PhraseEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PhraseJpaRepository extends ReactiveCrudRepository<PhraseEntity, UUID> {

    Mono<PhraseEntity> findByTextAndPartOfSpeech(String text, PartOfSpeech part);
}
