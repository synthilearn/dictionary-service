package com.synthilearn.dictionaryservice.infra.api.rest.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressEntry {
    @NotNull
    private UUID id;
    @NotNull
    private Integer newProgress;
}
