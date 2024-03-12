package com.synthilearn.dictionaryservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryParameters {

    private UUID id;
    private Boolean showTranslation;
    private String groups;
    private String partsOfSpeech;
    private LocalDate dateTo;
    private LocalDate dateFrom;
    private String phraseTypes;
}
