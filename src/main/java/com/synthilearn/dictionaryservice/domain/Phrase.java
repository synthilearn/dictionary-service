package com.synthilearn.dictionaryservice.domain;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"text", "type"})
public class Phrase implements Comparable<Phrase> {

    private UUID id;
    private UUID dictionaryId;
    private String text;
    private PhraseType type;
    private PhraseStatus status;
    private List<PhraseTranslate> phraseTranslates;

    public Phrase(String text, PhraseType type) {
        this.text = text;
        this.type = type;
    }

    @Override
    public int compareTo(Phrase o) {
        return this.text.compareTo(o.text);
    }
}
