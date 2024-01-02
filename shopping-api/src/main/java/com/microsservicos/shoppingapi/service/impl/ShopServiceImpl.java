package com.microsservicos.shoppingapi.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.DoubleStream;
import java.util.stream.DoubleStream.Builder;

import org.springframework.stereotype.Service;

import com.microsservicos.dto.ItemInputDto;
import com.microsservicos.dto.ProductDto;
import com.microsservicos.dto.ShopOutputDto;
import com.microsservicos.dto.ShopInputDto;
import com.microsservicos.exception.IllegalProductException;
import com.microsservicos.exception.ShopNotFoundException;
import com.microsservicos.exception.UserNotFoundException;
import com.microsservicos.shoppingapi.model.Item;
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
  public List<ShopOutputDto> getAll() {
    return shopRepository.findAll().stream().map(Mapper::shopToShopDto).map(ShopOutputDto::obfuscate).toList();
  }

  @Override
  public ShopOutputDto findById(Long id) {
    Shop shop = shopRepository.findById(id).orElseThrow(() -> new ShopNotFoundException());
    return Mapper.shopToShopDto(shop).obfuscate();
  }

  @Override
  public ShopOutputDto save(ShopInputDto shopDto, String key) {
    validateUserExistence(shopDto.userIdentifier(), key);
    List<ProductDto> allProducts = feacthAllProducts(shopDto.items());
    Double total = calculateTotal(allProducts, shopDto.items());
    return saveShop(shopDto, allProducts, total);
  }

  private void validateUserExistence(String cpf, String key) {
    if (Objects.isNull(userService.getUserByCpf(cpf, key))) {
      throw new UserNotFoundException();
    }
  }

  private List<ProductDto> feacthAllProducts(List<ItemInputDto> itens) {
    if (Objects.isNull(itens) || itens.isEmpty()) {
      throw new IllegalProductException();
    }
    List<ProductDto> allProducts = itens.stream().map(this::feacthProductIdentifier).toList();
    return allProducts;
  }

  private ProductDto feacthProductIdentifier(ItemInputDto item) {
    return feacthProduct(item.productIdentifier());
  }

  private ProductDto feacthProduct(String productIdentifier) {
    ProductDto product = productService.getProductByIdentifier(productIdentifier);
    if (Objects.isNull(product)) {
      throw new IllegalProductException();
    }
    return product;
  }

  private Double calculateTotal(List<ProductDto> allProducts, List<ItemInputDto> allItems) {
    Builder totalPriceStream = DoubleStream.builder();

    for(int i = 0; i < allProducts.size(); i++){
      totalPriceStream.add(allProducts.get(i).price() * allItems.get(i).amount());
    }
    
    return totalPriceStream.build().sum();
  }

  private ShopOutputDto saveShop(ShopInputDto shopDto, List<ProductDto> allProducts, Double total) {
    Shop shop = createEntityShop(shopDto, allProducts, total);

    return Mapper.shopToShopDto(shopRepository.save(shop));
  }

  private Shop createEntityShop(ShopInputDto shopDto, List<ProductDto> allProducts, Double total) {
    Shop shop = new Shop();
    shop.setTotal(total);
    shop.setDate(LocalDateTime.now());
    shop.setUserIdentifier(shopDto.userIdentifier());

    List<Item> allItem = new ArrayList<>();
    for (int i = 0; i < allProducts.size(); i++) {
      Item currentItem = new Item();
      currentItem.setProductIdentifier(shopDto.items().get(i).productIdentifier());
      currentItem.setPrice(allProducts.get(i).price());
      currentItem.setAmount(shopDto.items().get(i).amount());
      allItem.add(currentItem);
    }

    shop.setItens(allItem);
    return shop;
  }

  @Override
  public List<ShopOutputDto> getByUser(String userIdentifier, String key) {
    validateUserExistence(userIdentifier, key);
    return shopRepository.findAllByUserIdentifier(userIdentifier)
        .stream()
        .map(Mapper::shopToShopDto)
        .toList();
  }

  @Override
  public List<ShopOutputDto> getByDate(LocalDate date) {
    return shopRepository.findAllByDateAfter(date)
        .stream()
        .map(Mapper::shopToShopDto)
        .map(ShopOutputDto::obfuscate)
        .toList();
  }

}
