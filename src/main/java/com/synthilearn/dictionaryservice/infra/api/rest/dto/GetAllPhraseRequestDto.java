package com.synthilearn.dictionaryservice.infra.api.rest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

import com.synthilearn.dictionaryservice.domain.Groups;
import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.PhraseType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllPhraseRequestDto {
    @NotNull
    private UUID dictionaryId;
    private Integer page;
    private Integer size;
    private List<Groups> groups;
    private Boolean showTranslates; //
    private List<PartOfSpeech> partsOfSpeech; //
    private LocalDate startDate; //
    private LocalDate endDate; //
    private List<PhraseType> phraseTypes; //
}
