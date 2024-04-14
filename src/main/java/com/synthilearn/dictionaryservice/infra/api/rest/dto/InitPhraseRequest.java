package com.synthilearn.dictionaryservice.infra.api.rest.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.PhraseType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitPhraseRequest {

    @NotNull
    private UUID dictionaryId;
    @NotBlank
    private String text;
    @NotNull
    private PhraseType type;
    @NotNull
    private PartOfSpeech partOfSpeech;
    @NotNull
    private List<String> translations;
}