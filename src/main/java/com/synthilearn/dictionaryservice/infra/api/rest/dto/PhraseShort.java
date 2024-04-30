package com.synthilearn.dictionaryservice.infra.api.rest.dto;

import java.util.List;

import com.synthilearn.dictionaryservice.domain.PhraseType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhraseShort {

    private String text;
    private PhraseType type;
    private List<TranslationShort> phraseTranslates;
}
