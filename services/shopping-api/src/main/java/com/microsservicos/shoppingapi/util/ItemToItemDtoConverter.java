package com.microsservicos.shoppingapi.util;

import com.microsservicos.dto.ItemDto;
import com.microsservicos.shoppingapi.model.Item;

public class ItemToItemDtoConverter  {

  public static ItemDto convert(Item item) {
    return new ItemDto(item.getProductIdentifier(), item.getPrice());
  }

}
