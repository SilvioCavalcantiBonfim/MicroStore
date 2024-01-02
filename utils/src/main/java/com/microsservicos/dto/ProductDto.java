package com.microsservicos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDto(
    @NotBlank String productIdentifier,
    @NotBlank String name,
    @NotBlank String description,
    @NotNull Float price,
    @NotNull CategoryDto category) {
}