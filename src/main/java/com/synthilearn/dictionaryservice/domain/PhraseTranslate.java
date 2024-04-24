package com.synthilearn.dictionaryservice.domain;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhraseTranslate {

    private UUID id;
    private UUID phraseId;
    private String translationText;
    private PhraseStatus status;
    private ZonedDateTime creationDate;
}
