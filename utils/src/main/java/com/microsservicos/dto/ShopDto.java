package com.microsservicos.dto;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShopDto(
        @NotBlank String userIdentifier,
        @NotNull Double total,
        @NotNull Date date,
        List<ItemDto> itens) {
    public ShopDto obfuscate() {
        return new ShopDto(Obfuscator.obfuscate(userIdentifier), total, date, itens);
    }
}
