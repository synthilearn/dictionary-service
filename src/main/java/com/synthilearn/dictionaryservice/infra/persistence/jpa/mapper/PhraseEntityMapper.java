package com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper;

import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.PhraseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhraseEntityMapper {

    PhraseEntity map(Phrase phrase);
    Phrase map(PhraseEntity entity);
}
