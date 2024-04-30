package com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper;

import com.synthilearn.dictionaryservice.domain.PhraseTranslate;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.PhraseTranslateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhraseTranslateEntityMapper {

    PhraseTranslate map(PhraseTranslateEntity phraseTranslateEntity);
    PhraseTranslateEntity map(PhraseTranslate phraseTranslate);
}
