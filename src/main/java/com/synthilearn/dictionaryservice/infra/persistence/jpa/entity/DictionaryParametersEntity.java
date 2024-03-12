package com.synthilearn.dictionaryservice.infra.persistence.jpa.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table("dictionary_parameters")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DictionaryParametersEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    @Column("show_translation")
    private Boolean showTranslation;
    private String groups;
    @Column("parts_of_speech")
    private String partsOfSpeech;
    @Column("date_to")
    private LocalDate dateTo;
    @Column("date_from")
    private LocalDate dateFrom;
    @Column("phrase_types")
    private String phraseTypes;

    @Transient
    private boolean newRecord;

    @Override
    public boolean isNew() {
        return this.newRecord;
    }
}
