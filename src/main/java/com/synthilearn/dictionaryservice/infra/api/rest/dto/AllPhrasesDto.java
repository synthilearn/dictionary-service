package com.synthilearn.dictionaryservice.infra.api.rest.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllPhrasesDto<T> {
    private Integer totalPages;
    private T phrases;
}
