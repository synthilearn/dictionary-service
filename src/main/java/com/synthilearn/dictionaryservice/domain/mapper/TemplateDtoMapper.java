package com.synthilearn.dictionaryservice.domain.mapper;

import org.mapstruct.Mapper;

import com.synthilearn.dictionaryservice.domain.Template;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.CreateTemplateRequest;

@Mapper(componentModel = "spring")
public interface TemplateDtoMapper {

    Template map(CreateTemplateRequest request, byte[] file, Integer amountWords);
}
