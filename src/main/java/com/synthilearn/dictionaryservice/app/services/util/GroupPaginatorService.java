package com.synthilearn.dictionaryservice.app.services.util;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.util.CollectionUtils;

import com.synthilearn.dictionaryservice.domain.Groups;
import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class GroupPaginatorService {

    public static Object group(GetAllPhraseRequestDto requestDto, List<Phrase> phrases) {
        if (CollectionUtils.isEmpty(requestDto.getGroups())) {
            return phrases;
        }

        TreeMap<String, TreeMap<String, TreeSet<Phrase>>> externalMap =
                new TreeMap<>(Comparator.naturalOrder());

        for (Phrase phrase : phrases) {
//            MapKeys keys = getKeys(phrase, groups);
            MapKeys keys = null;

            TreeMap<String, TreeSet<Phrase>> internalMap = new TreeMap<>(Comparator.naturalOrder());
            internalMap.put(keys.getInternalKey(), new TreeSet<>() {{
                add(phrase);
            }});

            externalMap.merge(keys.getExternalKey(),
                    internalMap,
                    (oldValue, newValue) -> {
                        oldValue.merge(keys.getInternalKey(),
                                new TreeSet<>(Comparator.comparing(Phrase::getText)) {{
                                    add(phrase);
                                }}, (oldValueSet, newValueSet) -> {
                                    oldValueSet.addAll(newValueSet);
                                    return oldValueSet;
                                });
                        return oldValue;
                    });
        }

        return null;
    }


    private static MapKeys getKeys(Phrase phrase, List<Groups> groups) {
//        if (groups.getFirst().equals(Groups.LETTER)) {
//            return new MapKeys(phrase.getText().substring(0, 1).toUpperCase(),
//                    phrase.getPartOfSpeech().toString());
//        }
//        return new MapKeys(phrase.getPartOfSpeech().toString(),
//                phrase.getText().substring(0, 1).toUpperCase());
        return null;
    }

    @Getter
    @AllArgsConstructor
    static class MapKeys {
        private String externalKey;
        private String internalKey;
    }

}
