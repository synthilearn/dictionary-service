package com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper;

import com.synthilearn.dictionaryservice.domain.Dictionary;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.DictionaryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DictionaryEntityMapper {

    Dictionary map(DictionaryEntity entity);
}
