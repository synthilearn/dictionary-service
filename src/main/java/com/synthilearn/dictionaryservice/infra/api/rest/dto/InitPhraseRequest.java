package com.synthilearn.dictionaryservice.infra.api.rest.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
    private UUID id;
    @NotBlank
    private String text;
    @NotNull
    private PhraseType type;
    @NotNull
    @Valid
    @Size(min = 1)
    private List<TranslationShort> phraseTranslates;
}