package com.synthilearn.dictionaryservice.infra.persistence.jpa.repository;

import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.DictionaryEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface DictionaryJpaRepository extends ReactiveCrudRepository<DictionaryEntity, UUID> {
    Mono<DictionaryEntity> findByWorkareaId(UUID workareaId);
}
