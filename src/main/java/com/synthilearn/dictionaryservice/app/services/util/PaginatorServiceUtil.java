package com.synthilearn.dictionaryservice.app.services.util;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.synthilearn.dictionaryservice.domain.Phrase;
import com.synthilearn.dictionaryservice.infra.api.rest.dto.GetAllPhraseRequestDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class PaginatorServiceUtil {

    public static PaginateInfo paginate(Object phrases,
                                        GetAllPhraseRequestDto requestDto) {
        return switch (requestDto.getGroups().size()) {
            case 0 -> paginateEmptyGroupLevel(phrases, requestDto);
            case 1 -> paginateOneGroupLevel(phrases, requestDto);
            default -> new PaginateInfo(1000, phrases);
        };
    }

    private static PaginateInfo paginateOneGroupLevel(Object phrases,
                                                     GetAllPhraseRequestDto requestDto) {
        Map<String, TreeSet<Phrase>> phrasesTree = (Map<String, TreeSet<Phrase>>) phrases;
        Map<String, TreeSet<Phrase>> newPhrasesTree = new TreeMap<>();

        int offsetCounter = 0;
        int innerOffset = 0;
        int sizeCounter = 0;
        int offset = requestDto.getPage() * requestDto.getSize();
        boolean innerOffsetUsed = false;
        for (Map.Entry<String, TreeSet<Phrase>> entry : phrasesTree.entrySet()) {
            int phrasesSize = entry.getValue().size();
            if (++offsetCounter + phrasesSize <= offset) {
                offsetCounter += phrasesSize;
                continue;
            }

            if (!innerOffsetUsed) {
                innerOffset = offset - offsetCounter;
                innerOffsetUsed = true;
            }

            if (++sizeCounter <= requestDto.getSize()) {
                newPhrasesTree.put(entry.getKey(), new TreeSet<>(entry.getValue().stream()
                        .skip(Math.max(innerOffset, 0))
                        .limit(requestDto.getSize() - sizeCounter)
                        .collect(Collectors.toSet())));
                sizeCounter += phrasesSize;
                continue;
            }
            break;
        }

        int totalElements = phrasesTree.values().stream().mapToInt(innerPhrases -> innerPhrases.size() + 1).sum();
        int totalPages = (int) Math.ceil((double) totalElements / requestDto.getSize());

        return new PaginateInfo(totalPages, newPhrasesTree);
    }

    private static PaginateInfo paginateEmptyGroupLevel(Object phrasesObj,
                                                        GetAllPhraseRequestDto requestDto) {
        List<Phrase> phrases = (List<Phrase>) phrasesObj;
        int totalPages = (int) Math.ceil((double) phrases.size() / requestDto.getSize());
        return new PaginateInfo(totalPages, phrases.stream()
                .skip((long) requestDto.getPage() * requestDto.getSize())
                .limit(requestDto.getSize())
                .toList());
    }
}
