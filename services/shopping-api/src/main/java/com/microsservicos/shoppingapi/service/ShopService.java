package com.microsservicos.shoppingapi.service;

import java.util.List;

import com.microsservicos.shoppingapi.dto.ShopDto;

public interface ShopService {
  List<ShopDto> getAll();
  ShopDto findById(Long id);
  ShopDto save(ShopDto shopDto);
  List<ShopDto> getByUser(String userIdentifier);
  List<ShopDto> getByDate(ShopDto shopDto);
}
