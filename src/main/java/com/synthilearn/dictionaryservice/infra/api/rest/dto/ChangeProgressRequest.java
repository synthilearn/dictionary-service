package com.synthilearn.dictionaryservice.infra.api.rest.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProgressRequest {
    @NotNull
    @Size(min = 1)
    private List<ProgressEntry> progressEntries;
}
