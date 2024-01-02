package com.microsservicos.shoppingapi.service;

import java.time.LocalDate;
import java.util.List;

import com.microsservicos.dto.ShopOutputDto;
import com.microsservicos.dto.ShopInputDto;

public interface ShopService {
  List<ShopOutputDto> getAll();
  ShopOutputDto findById(Long id);
  ShopOutputDto save(ShopInputDto shopDto, String key);
  List<ShopOutputDto> getByUser(String userIdentifier, String key);
  List<ShopOutputDto> getByDate(LocalDate date);
}
