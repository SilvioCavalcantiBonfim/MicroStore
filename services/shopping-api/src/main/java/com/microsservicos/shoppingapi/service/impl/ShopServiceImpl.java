package com.microsservicos.shoppingapi.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.microsservicos.shoppingapi.dto.ItemDto;
import com.microsservicos.shoppingapi.dto.ShopDto;
import com.microsservicos.shoppingapi.model.Shop;
import com.microsservicos.shoppingapi.repository.ShopRepository;
import com.microsservicos.shoppingapi.service.ShopService;

@Service
public class ShopServiceImpl implements ShopService {

  private final ShopRepository shopRepository;

  public ShopServiceImpl(ShopRepository shopRepository) {
    this.shopRepository = shopRepository;
  }

  @Override
  public List<ShopDto> getAll() {
    return shopRepository.findAll().stream().map(ShopDto::convert).toList();
  }

  @Override
  public ShopDto findById(Long id) {
    Optional<Shop> shop = shopRepository.findById(id);
    if (shop.isPresent()) {
      return ShopDto.convert(shop.get());
    }
    return null;
  }

  @Override
  public ShopDto save(ShopDto shopDto) {

    Double total = shopDto.items().stream()
    .mapToDouble(ItemDto::price).sum();

    Shop shop = Shop.convert(shopDto);
    shop.setTotal(total);
    shop.setDate(new Date());
    return ShopDto.convert(shopRepository.save(shop));
  }

  @Override
  public List<ShopDto> getByUser(String userIdentifier) {
    return shopRepository.findAllByUserIdentifier(userIdentifier).stream().map(ShopDto::convert).toList();
  }

  @Override
  public List<ShopDto> getByDate(ShopDto shopDto) {
    return shopRepository.findAllByDateAfter(shopDto.date()).stream().map(ShopDto::convert).toList();
  }

}
