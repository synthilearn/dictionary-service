package com.synthilearn.dictionaryservice.infra.persistence.jpa.repository;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.PhraseEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PhraseJpaRepository extends ReactiveCrudRepository<PhraseEntity, UUID> {

    Mono<PhraseEntity> findFirstByTextAndDictionaryId(String text, UUID dictionaryId);

    @Query("""
            SELECT DISTINCT p.* FROM phrase p
                INNER JOIN phrase_translate pt on p.id = pt.phrase_id
                AND pt.part_of_speech in (:partsOfSpeech)
            WHERE p.dictionary_id = :dictionaryId
            AND p.type in (:types)
            AND p.creation_date BETWEEN :startDate AND :endDate
            ORDER BY p.creation_date DESC
            """)
    Flux<Phrase> findAllPhrasesWithTranslations(UUID dictionaryId, Set<PartOfSpeech> partsOfSpeech,
                                                Set<String> types, ZonedDateTime startDate,
                                                ZonedDateTime endDate);
}
