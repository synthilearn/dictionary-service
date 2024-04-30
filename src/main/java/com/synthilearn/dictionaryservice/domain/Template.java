package com.synthilearn.dictionaryservice.domain;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Template {

    private UUID id;
    private String name;
    private byte[] file;
    private Integer amountWords;
    private ZonedDateTime creationDate;
}