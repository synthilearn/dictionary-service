package com.synthilearn.dictionaryservice.app.services.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginateInfo {
    private Integer totalPages;
    private Object phrases;
}
