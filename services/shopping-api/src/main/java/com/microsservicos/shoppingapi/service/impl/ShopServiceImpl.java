package com.microsservicos.shoppingapi.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.microsservicos.dto.ItemDto;
import com.microsservicos.dto.ProductDto;
import com.microsservicos.dto.ShopDto;
import com.microsservicos.exception.IllegalProductException;
import com.microsservicos.exception.ShopNotFoundException;
import com.microsservicos.exception.UserNotFoundException;
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
    Shop shop = shopRepository.findById(id).orElseThrow(() -> new ShopNotFoundException());
    return ShopToShopDtoConverter.convert(shop);
  }

  @Override
  public ShopDto save(ShopDto shopDto, String key) {
    validateUserExistence(shopDto, key);
    List<ProductDto> allProducts = feacthAllProducts(shopDto.items());
    Double total = calculateTotal(allProducts);
    return saveShop(shopDto, total);
  }

  private void validateUserExistence(ShopDto shopDto, String key) {
    if (Objects.isNull(userService.getUserByCpf(shopDto.userIdentifier(), key))) {
      throw new UserNotFoundException();
    }
  }

  private List<ProductDto> feacthAllProducts(List<ItemDto> itens){
    List<ProductDto> allProducts = itens.stream().map(this::feacthProduct).toList();
    return allProducts;
  }

  private ProductDto feacthProduct(ItemDto itemDto){
    ProductDto product = productService.getProductByIdentifier(itemDto.productIdentifier());
    if (Objects.isNull(product)) {
      throw new IllegalProductException();
    }
    return product;
  }


  private Double calculateTotal(List<ProductDto> allProducts) {
    Double total = allProducts.stream().mapToDouble(ProductDto::price).sum();
    return total;
  }

  private ShopDto saveShop(ShopDto shopDto, Double total) {
    Shop shop = Shop.convert(shopDto);
    shop.setTotal(total);
    shop.setDate(new Date());
    return ShopToShopDtoConverter.convert(shopRepository.save(shop));
  }

  @Override
  public List<ShopDto> getByUser(String userIdentifier) {
    return shopRepository.findAllByUserIdentifier(userIdentifier)
    .stream()
    .map(ShopToShopDtoConverter::convert)
    .toList();
  }

  @Override
  public List<ShopDto> getByDate(ShopDto shopDto) {
    return shopRepository.findAllByDateAfter(shopDto.date())
    .stream()
    .map(ShopToShopDtoConverter::convert)
    .toList();
  }

}
