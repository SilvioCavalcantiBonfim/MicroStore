package com.microsservicos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemInputDto(@NotBlank String productIdentifier, @NotNull Integer amount) {}