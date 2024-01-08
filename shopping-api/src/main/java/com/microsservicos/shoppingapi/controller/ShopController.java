package com.microsservicos.shoppingapi.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microsservicos.dto.ShopOutputDto;
import com.microsservicos.dto.ShopInputDto;
import com.microsservicos.shoppingapi.service.ShopService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/shopping")
public class ShopController {

  private final ShopService shopService;

  public ShopController(ShopService shopService) {
    this.shopService = shopService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ShopOutputDto> retrieveAllShop() {
    return shopService.retrieveAllShops();
  }

  @GetMapping("/shopByUser/{userIdentifier}")
  @ResponseStatus(HttpStatus.OK)
  public List<ShopOutputDto> retrieveByUserIdentifier(@PathVariable(name = "userIdentifier") String userIdentifier,
      @RequestHeader(name = "key", required = true) String key) {
    return shopService.retrieveShopsByUser(userIdentifier, key);
  }

  @GetMapping("/shopByDate")
  @ResponseStatus(HttpStatus.OK)
  public List<ShopOutputDto> retrieveByDate(
      @RequestParam(name = "after", required = true) 
      @DateTimeFormat(iso = ISO.DATE) 
      final LocalDate date
  ) {
    return shopService.retrieveShopsByDate(date);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ShopOutputDto retrieveById(@PathVariable(name = "id") Long id) {
    return shopService.retrieveShopById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ShopOutputDto addNewShop(@Valid @RequestBody ShopInputDto shopDto,
      @RequestHeader(name = "key", required = true) String key) {
    return shopService.addShop(shopDto, key);
  }
}
