package com.microsservicos.shoppingapi.dto;

import java.util.Date;
import java.util.List;

import com.microsservicos.shoppingapi.model.Shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShopDto(
    @NotBlank String userIdentifier,
    @NotNull Double total,
    @NotNull Date date,
    List<ItemDto> items) {

  public static ShopDto convert(Shop shop) {
    return new ShopDto(shop.getUserIdentifier(), shop.getTotal(), shop.getDate(),
        shop.getItems().stream().map(ItemDto::convert).toList());
  }
}
