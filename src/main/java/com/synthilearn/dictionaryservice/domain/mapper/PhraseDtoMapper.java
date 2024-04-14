package com.synthilearn.dictionaryservice.domain.mapper;

import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.InitPhraseRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhraseDtoMapper {

    @Mapping(target = "status", ignore = true)
    Phrase map(InitPhraseRequest request);
}
