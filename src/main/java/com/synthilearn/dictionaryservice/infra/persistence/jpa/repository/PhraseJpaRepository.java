package com.synthilearn.dictionaryservice.infra.persistence.jpa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.synthilearn.dictionaryservice.domain.Groups;
import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.PhraseEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PhraseJpaRepository extends ReactiveCrudRepository<PhraseEntity, UUID> {

    Mono<PhraseEntity> findByTextAndPartOfSpeech(String text, PartOfSpeech part);

    @Query("""
            SELECT p.* FROM phrase p
                WHERE p.dictionary_id = :dictionaryId
            ORDER BY p.creation_date DESC
            """)
    Flux<Phrase> findAllPhrasesWithTranslations(UUID dictionaryId);
}
