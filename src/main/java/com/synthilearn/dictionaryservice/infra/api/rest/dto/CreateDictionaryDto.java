package com.synthilearn.dictionaryservice.infra.api.rest.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDictionaryDto(@NotNull UUID workareaId) {
}
