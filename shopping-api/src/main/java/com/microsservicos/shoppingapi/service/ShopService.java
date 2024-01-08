package com.microsservicos.shoppingapi.service;

import java.time.LocalDate;
import java.util.List;

import com.microsservicos.dto.ShopOutputDto;
import com.microsservicos.dto.ShopInputDto;

public interface ShopService {
  List<ShopOutputDto> retrieveAllShops();
  ShopOutputDto retrieveShopById(Long id);
  ShopOutputDto addShop(ShopInputDto shopDto, String key);
  List<ShopOutputDto> retrieveShopsByUser(String userIdentifier, String key);
  List<ShopOutputDto> retrieveShopsByDate(LocalDate date);
}
