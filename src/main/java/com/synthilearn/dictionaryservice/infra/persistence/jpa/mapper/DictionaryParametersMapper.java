package com.synthilearn.dictionaryservice.infra.persistence.jpa.mapper;

import java.util.ArrayList;
import java.util.List;

import com.synthilearn.dictionaryservice.domain.DictionaryParameters;
import com.synthilearn.dictionaryservice.domain.Groups;
import com.synthilearn.dictionaryservice.domain.PartOfSpeech;
import com.synthilearn.dictionaryservice.domain.PhraseType;
import com.synthilearn.dictionaryservice.infra.persistence.jpa.entity.DictionaryParametersEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DictionaryParametersMapper {


    DictionaryParameters map(DictionaryParametersEntity entity);

    default List<Groups> mapStringToGroupsList(String string) {
        if (string == null) {
            return new ArrayList<>();
        }
        String[] groupNames = string.substring(1, string.length() - 1).split(", ");
        List<Groups> groupsList = new ArrayList<>();
        for (String groupName : groupNames) {
            groupsList.add(Groups.valueOf(groupName.trim()));
        }
        return groupsList;
    }

    default List<PhraseType> mapStringToTypeList(String string) {
        if (string == null) {
            return new ArrayList<>();
        }
        String[] groupNames = string.substring(1, string.length() - 1).split(", ");
        List<PhraseType> groupsList = new ArrayList<>();
        for (String groupName : groupNames) {
            groupsList.add(PhraseType.valueOf(groupName.trim()));
        }
        return groupsList;
    }

    default List<PartOfSpeech> mapStringToPartsOfSpeechList(String string) {
        if (string == null) {
            return new ArrayList<>();
        }
        String[] groupNames = string.substring(1, string.length() - 1).split(", ");
        List<PartOfSpeech> groupsList = new ArrayList<>();
        for (String groupName : groupNames) {
            groupsList.add(PartOfSpeech.valueOf(groupName.trim()));
        }
        return groupsList;
    }
}
