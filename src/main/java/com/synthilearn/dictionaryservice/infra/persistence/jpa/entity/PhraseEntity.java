package com.synthilearn.dictionaryservice.infra.persistence.jpa.entity;

import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.PhraseStatus;
import com.synthilearn.dictionaryservice.domain.PhraseType;
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

@Table("phrase")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhraseEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    @Column("dictionary_id")
    private UUID dictionaryId;
    private String text;
    private PhraseType type;
    @Column("part_of_speech")
    private PartOfSpeech partOfSpeech;
    @Column("creation_date")
    private ZonedDateTime creationDate;
    @Column("updated_date")
    private ZonedDateTime updatedDate;
    private PhraseStatus status;

    @Transient
    private boolean newRecord;

    @Override
    public boolean isNew() {
        return this.newRecord;
    }
}
