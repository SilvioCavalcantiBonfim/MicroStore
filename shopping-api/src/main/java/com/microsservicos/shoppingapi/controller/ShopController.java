package com.microsservicos.shoppingapi.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.microsservicos.dto.ShopDto;
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
  public List<ShopDto> getAll() {
    return shopService.getAll();
  }

  @GetMapping("/shopByUser/{userIdentifier}")
  @ResponseStatus(HttpStatus.OK)
  public List<ShopDto> getByUser(@PathVariable(name = "userIdentifier") String userIdentifier,
      @RequestHeader(name = "key", required = true) String key) {
    return shopService.getByUser(userIdentifier, key);
  }

  @GetMapping("/shopByDate")
  @ResponseStatus(HttpStatus.OK)
  public List<ShopDto> getByDate(
      @RequestParam(name = "after", required = true) @DateTimeFormat(pattern = "dd-MM-yyyy") Date date) {
    return shopService.getByDate(date);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ShopDto getById(@PathVariable(name = "id") Long id) {
    return shopService.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ShopDto createShop(@Valid @RequestBody ShopDto shopDto,
      @RequestHeader(name = "key", required = true) String key) {
    return shopService.save(shopDto, key);
  }
}
