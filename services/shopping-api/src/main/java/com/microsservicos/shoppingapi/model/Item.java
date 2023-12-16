package com.microsservicos.shoppingapi.model;

import com.microsservicos.dto.ItemDto;

import jakarta.persistence.Embeddable;

@Embeddable
public class Item {

  private String productIdentifier;
  private Float price;

  public String getProductIdentifier() {
    return productIdentifier;
  }
  public void setProductIdentifier(String productIdentifier) {
    this.productIdentifier = productIdentifier;
  }
  public Float getPrice() {
    return price;
  }
  public void setPrice(Float price) {
    this.price = price;
  }

  public static Item convert(ItemDto itemDto){
    Item item = new Item();
    item.setProductIdentifier(itemDto.productIdentifier());
    item.setPrice(itemDto.price());
    return item;
  }
}
