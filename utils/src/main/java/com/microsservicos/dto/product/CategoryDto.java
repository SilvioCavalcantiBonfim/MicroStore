package com.microsservicos.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryDto(@NotNull Long id, @NotBlank String name) {}
