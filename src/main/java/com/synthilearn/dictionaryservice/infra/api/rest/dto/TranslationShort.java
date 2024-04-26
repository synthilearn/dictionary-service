package com.synthilearn.dictionaryservice.infra.api.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.synthilearn.dictionaryservice.domain.PartOfSpeech;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationShort {
    @NotNull
    private PartOfSpeech partOfSpeech;
    @NotBlank
    private String translationText;
}
