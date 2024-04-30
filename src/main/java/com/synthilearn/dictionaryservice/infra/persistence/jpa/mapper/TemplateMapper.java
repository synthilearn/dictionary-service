package com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper;

import org.mapstruct.Mapper;

import com.synthilearn.dictionaryservice.domain.Template;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.TemplateEntity;

@Mapper(componentModel = "spring")
public interface TemplateMapper {

    Template map(TemplateEntity templateEntity);
    TemplateEntity map(Template template);
}
