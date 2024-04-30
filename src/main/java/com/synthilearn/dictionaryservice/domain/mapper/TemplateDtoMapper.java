package com.synthilearn.dictionaryservice.domain.mapper;

import org.mapstruct.Mapper;

import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.domain.Template;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.CreateTemplateRequest;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.PhraseShort;

@Mapper(componentModel = "spring")
public interface TemplateDtoMapper {

    Template map(CreateTemplateRequest request, byte[] file, Integer amountWords);

    PhraseShort map(Phrase phrase);
}
