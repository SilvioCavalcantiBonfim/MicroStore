package com.microsservicos.shoppingapi.dto;

import com.microsservicos.shoppingapi.model.Item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemDto(
    @NotBlank String productIdentifier,
    @NotNull Float price) {

  public static ItemDto convert(Item item) {
    return new ItemDto(item.getProductIdentifier(), item.getPrice());
  }

}
