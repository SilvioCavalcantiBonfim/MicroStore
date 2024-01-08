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
  public List<ShopOutputDto> retrieveAllShops() {
    return shopRepository.findAll().stream().map(Mapper::convertShopToDto).map(ShopOutputDto::obfuscate).toList();
  }

  @Override
  public ShopOutputDto retrieveShopById(Long id) {
    Shop shop = shopRepository.findById(id).orElseThrow(() -> new ShopNotFoundException());
    return Mapper.convertShopToDto(shop).obfuscate();
  }

  @Override
  public ShopOutputDto addShop(ShopInputDto shopDto, String key) {
    validateUserExistence(shopDto.userIdentifier(), key);
    List<ProductDto> allProducts = fetchAllProducts(shopDto.items());
    Double total = calculateTotal(allProducts, shopDto.items());
    return persistShop(shopDto, allProducts, total);
  }

  private void validateUserExistence(String cpf, String key) {
    if (Objects.isNull(userService.retrieveUserByCpf(cpf, key))) {
      throw new UserNotFoundException();
    }
  }

  private List<ProductDto> fetchAllProducts(List<ItemInputDto> itens) {
    if (Objects.isNull(itens) || itens.isEmpty()) {
      throw new IllegalProductException();
    }
    List<ProductDto> allProducts = itens.stream().map(this::fetchProductByIdentifier).toList();
    return allProducts;
  }

  private ProductDto fetchProductByIdentifier(ItemInputDto item) {
    return fetchProduct(item.productIdentifier());
  }

  private ProductDto fetchProduct(String productIdentifier) {
    ProductDto product = productService.retrieveProductByIdentifier(productIdentifier);
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

  private ShopOutputDto persistShop(ShopInputDto shopDto, List<ProductDto> allProducts, Double total) {
    Shop shop = createShopEntity(shopDto, allProducts, total);

    return Mapper.convertShopToDto(shopRepository.save(shop));
  }

  private Shop createShopEntity(ShopInputDto shopDto, List<ProductDto> allProducts, Double total) {
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
  public List<ShopOutputDto> retrieveShopsByUser(String userIdentifier, String key) {
    validateUserExistence(userIdentifier, key);
    return shopRepository.findAllByUserIdentifier(userIdentifier)
        .stream()
        .map(Mapper::convertShopToDto)
        .toList();
  }

  @Override
  public List<ShopOutputDto> retrieveShopsByDate(LocalDate date) {
    return shopRepository.findAllByDateAfter(date.atStartOfDay())
        .stream()
        .map(Mapper::convertShopToDto)
        .map(ShopOutputDto::obfuscate)
        .toList();
  }

}
