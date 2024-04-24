package com.synthilearn.dictionaryservice.domain;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Phrase implements Comparable<Phrase> {

    private UUID id;
    private UUID dictionaryId;
    private String text;
    private PhraseType type;
    private PartOfSpeech partOfSpeech;
    private PhraseStatus status;
    private List<PhraseTranslate> phraseTranslates;

    @Override
    public int compareTo(Phrase o) {
        return this.text.compareTo(o.text);
    }
}
