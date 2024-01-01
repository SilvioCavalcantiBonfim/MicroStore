package com.microsservicos.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ShopInputDto(
        @NotBlank(message = Message.CPF_BLANK) @Pattern(regexp = "^\\d{11}$", message = Message.CPF_LENGTH) String userIdentifier,
        @NotNull List<ItemInputDto> items) {
}
