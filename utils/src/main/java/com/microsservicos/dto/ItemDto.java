package com.microsservicos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemDto(@NotBlank String productIdentifier, @NotNull Float price) {}