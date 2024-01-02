package com.microsservicos.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShopOutputDto(
        @NotBlank String userIdentifier,
        @NotNull Double total,
        @NotNull LocalDateTime date,
        List<ItemDto> itens) {
    public ShopOutputDto obfuscate() {
        return new ShopOutputDto(Obfuscator.obfuscate(userIdentifier), total, date, itens);
    }
}
