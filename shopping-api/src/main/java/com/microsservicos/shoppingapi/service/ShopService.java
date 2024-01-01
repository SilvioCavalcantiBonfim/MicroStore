package com.microsservicos.shoppingapi.service;

import java.util.Date;
import java.util.List;

import com.microsservicos.dto.ShopDto;
import com.microsservicos.dto.ShopInputDto;

public interface ShopService {
  List<ShopDto> getAll();
  ShopDto findById(Long id);
  ShopDto save(ShopInputDto shopDto, String key);
  List<ShopDto> getByUser(String userIdentifier, String key);
  List<ShopDto> getByDate(Date date);
}
