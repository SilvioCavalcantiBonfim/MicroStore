package com.microsservicos.shoppingapi.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.microsservicos.dto.ItemDto;
import com.microsservicos.dto.ShopDto;
import com.microsservicos.shoppingapi.model.Shop;
import com.microsservicos.shoppingapi.repository.ShopRepository;
import com.microsservicos.shoppingapi.service.ProductService;
import com.microsservicos.shoppingapi.service.ShopService;
import com.microsservicos.shoppingapi.service.UserService;
import com.microsservicos.shoppingapi.util.ShopToShopDtoConverter;

@Service
public class ShopServiceImpl implements ShopService {

  private final ShopRepository shopRepository;
  private final UserService userService;
  private final ProductService productService;

  public ShopServiceImpl(ShopRepository shopRepository, UserService userService, ProductService productService) {
    this.shopRepository = shopRepository;
    this.userService = userService;
    this.productService = productService;
  }

  @Override
  public List<ShopDto> getAll() {
    return shopRepository.findAll().stream().map(ShopToShopDtoConverter::convert).toList();
  }

  @Override
  public ShopDto findById(Long id) {
    Optional<Shop> shop = shopRepository.findById(id);
    if (shop.isPresent()) {
      return ShopToShopDtoConverter.convert(shop.get());
    }
    return null;
  }

  @Override
  public ShopDto save(ShopDto shopDto) {

    if (userService.getUserByCpf(shopDto.userIdentifier()) == null) {
      return null;
    }

    if (!validateProduct(shopDto.items())) {
      return null;
    }

    Double total = shopDto.items().stream()
    .mapToDouble(ItemDto::price).sum();

    Shop shop = Shop.convert(shopDto);
    shop.setTotal(total);
    shop.setDate(new Date());
    return ShopToShopDtoConverter.convert(shopRepository.save(shop));
  }

  private boolean validateProduct(List<ItemDto> itens){
    return itens.stream().map(item -> productService.getProductByIdentifier(item.productIdentifier())).allMatch(product -> product != null);
  }

  @Override
  public List<ShopDto> getByUser(String userIdentifier) {
    return shopRepository.findAllByUserIdentifier(userIdentifier).stream().map(ShopToShopDtoConverter::convert).toList();
  }

  @Override
  public List<ShopDto> getByDate(ShopDto shopDto) {
    return shopRepository.findAllByDateAfter(shopDto.date()).stream().map(ShopToShopDtoConverter::convert).toList();
  }

}
