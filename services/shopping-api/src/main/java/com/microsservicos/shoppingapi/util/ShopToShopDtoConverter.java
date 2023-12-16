package com.microsservicos.shoppingapi.util;

import com.microsservicos.dto.ShopDto;
import com.microsservicos.shoppingapi.model.Shop;

public class ShopToShopDtoConverter {

  public static ShopDto convert(Shop shop) {
    return new ShopDto(shop.getUserIdentifier(), shop.getTotal(), shop.getDate(),
        shop.getItems().stream().map(ItemToItemDtoConverter::convert).toList());
  }
}
