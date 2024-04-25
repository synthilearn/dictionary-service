package com.synthilearn.dictionaryservice.infra.api.rest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
    @NotNull
    private List<Groups> groups;
    @NotNull
    private Boolean showTranslates;
    @NotNull
    @Size(min = 1)
    private Set<PartOfSpeech> partsOfSpeech;
    private LocalDate startDate = LocalDate.ofYearDay(1900, 1);
    private LocalDate endDate = LocalDate.ofYearDay(2100, 1);
    @NotNull
    @Size(min = 1)
    private List<PhraseType> phraseTypes;
}
