package com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper;

import com.synthilearn.dictionaryservice.domain.DictionaryParameters;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.DictionaryParametersEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DictionaryParametersMapper {

    DictionaryParameters map(DictionaryParametersEntity entity);
}
