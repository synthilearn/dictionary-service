package com.synthilearn.dictionaryservice.infra.persistence.jpa.entity;


import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.PhraseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table("phrase_translate")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhraseTranslateEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    @Column("phrase_id")
    private UUID phraseId;
    @Column("translation_text")
    private String translationText;
    @Column("creation_date")
    private ZonedDateTime creationDate;
    @Column("part_of_speech")
    private PartOfSpeech partOfSpeech;
    @Column("updated_date")
    private ZonedDateTime updatedDate;
    private PhraseStatus status;
    @Column("learn_level")
    private Integer learnLevel;

    @Transient
    private boolean newRecord;

    @Override
    public boolean isNew() {
        return this.newRecord;
    }
}
