package com.microsservicos.shoppingapi.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.microsservicos.dto.ItemDto;
import com.microsservicos.dto.ShopOutputDto;
import com.microsservicos.shoppingapi.model.Item;
import com.microsservicos.shoppingapi.model.Shop;

import jakarta.validation.Valid;

public final class Mapper{

  private Mapper(){}

  public static ItemDto itemToItemDto(Item item) {
    try {
      return new ItemDto(item.getProductIdentifier(), item.getPrice(), item.getAmount());
    } catch (RuntimeException e) {
      throw new IllegalArgumentException("Invalid argument provided.");
    }
  }

  public static Item itemDtoToItem(@Valid ItemDto itemDto){
    try {
      Item item = new Item();
      item.setProductIdentifier(itemDto.productIdentifier());
      item.setPrice(itemDto.price());
      item.setAmount(itemDto.amount());
      return item;
    } catch (RuntimeException e) {
      throw new IllegalArgumentException("Invalid argument provided.");
    }
  }

  public static ShopOutputDto shopToShopDto(Shop shop){
    try {
      List<ItemDto> itens = new ArrayList<>();
      if (Objects.nonNull(shop.getItens())) {
        shop.getItens().stream().map(Mapper::itemToItemDto).forEach(itens::add);
      }
      return new ShopOutputDto(shop.getUserIdentifier(), shop.getTotal(), shop.getDate(),
          itens);
    } catch (RuntimeException e) {
      throw new IllegalArgumentException("Invalid argument provided.");
    }
  }
  
  public static Shop shopDtoToShop(@Valid ShopOutputDto shopDto){
    try {
      Shop shop = new Shop();
      shop.setUserIdentifier(shopDto.userIdentifier());
      shop.setTotal(shopDto.total());
      shop.setDate(shopDto.date());
  
      List<Item> itens = new ArrayList<>();
  
      if(Objects.nonNull(shopDto.itens())){
        shopDto.itens().stream().map(Mapper::itemDtoToItem).forEach(itens::add);
      }
  
      shop.setItens(Collections.unmodifiableList(itens));
      return shop;
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
      throw new IllegalArgumentException("Invalid argument provided.");
    }
  }
}
