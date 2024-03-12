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

import java.util.UUID;

@Table("dictionary")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DictionaryEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    @Column("workarea_id")
    private UUID workareaId;

    @Transient
    private boolean newRecord;

    @Override
    public boolean isNew() {
        return this.newRecord;
    }
}
