package com.microsservicos.shoppingapi.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.microsservicos.dto.ItemDto;
import com.microsservicos.dto.ShopDto;
import com.microsservicos.dto.product.ProductDto;
import com.microsservicos.exception.IllegalProductException;
import com.microsservicos.exception.ShopNotFoundException;
import com.microsservicos.exception.UserNotFoundException;
import com.microsservicos.shoppingapi.model.Shop;
import com.microsservicos.shoppingapi.repository.ShopRepository;
import com.microsservicos.shoppingapi.service.ProductService;
import com.microsservicos.shoppingapi.service.ShopService;
import com.microsservicos.shoppingapi.service.UserService;
import com.microsservicos.shoppingapi.util.Mapper;

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
    return shopRepository.findAll().stream().map(Mapper::shopToShopDto).map(ShopDto::obfuscate).toList();
  }

  @Override
  public ShopDto findById(Long id) {
    Shop shop = shopRepository.findById(id).orElseThrow(() -> new ShopNotFoundException());
    return Mapper.shopToShopDto(shop).obfuscate();
  }

  @Override
  public ShopDto save(ShopDto shopDto, String key) {
    validateUserExistence(shopDto.userIdentifier(), key);
    List<ProductDto> allProducts = feacthAllProducts(shopDto.itens());
    Double total = calculateTotal(allProducts);
    return saveShop(shopDto, total);
  }

  private void validateUserExistence(String cpf, String key) {
    if (Objects.isNull(userService.getUserByCpf(cpf, key))) {
      throw new UserNotFoundException();
    }
  }

  private List<ProductDto> feacthAllProducts(List<ItemDto> itens){
    if (Objects.isNull(itens) || itens.isEmpty()) {
      throw new IllegalProductException();
    }
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
    Shop shop = Mapper.shopDtoToShop(shopDto);
    shop.setTotal(total);
    shop.setDate(new Date());
    return Mapper.shopToShopDto(shopRepository.save(shop));
  }

  @Override
  public List<ShopDto> getByUser(String userIdentifier, String key) {
    validateUserExistence(userIdentifier, key);
    return shopRepository.findAllByUserIdentifier(userIdentifier)
    .stream()
    .map(Mapper::shopToShopDto)
    .toList();
  }

  @Override
  public List<ShopDto> getByDate(Date date) {
    return shopRepository.findAllByDateAfter(date)
    .stream()
    .map(Mapper::shopToShopDto)
    .map(ShopDto::obfuscate)
    .toList();
  }

}
