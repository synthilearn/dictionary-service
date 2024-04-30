package com.synthilearn.dictionaryservice.infra.persistence.jpa.entity;


import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("template")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TemplateEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    private String name;
    private byte[] file;
    @Column("created_date")
    private ZonedDateTime creationDate;
    @Column("amount_of_words")
    private Integer amountWords;

    @Transient
    private boolean newRecord;

    @Override
    public boolean isNew() {
        return this.newRecord;
    }
}