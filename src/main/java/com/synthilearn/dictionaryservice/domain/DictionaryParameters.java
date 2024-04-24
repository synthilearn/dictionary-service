package com.synthilearn.dictionaryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryParameters {

    private UUID id;
    private Boolean showTranslation;
    private List<Groups> groups;
    private List<PartOfSpeech> partsOfSpeech;
    private LocalDate dateTo;
    private LocalDate dateFrom;
    private List<PhraseType> phraseTypes;
}
