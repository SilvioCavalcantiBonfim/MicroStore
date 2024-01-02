package com.microsservicos.shoppingapi.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
  public List<ShopOutputDto> getAll() {
    return shopService.getAll();
  }

  @GetMapping("/shopByUser/{userIdentifier}")
  @ResponseStatus(HttpStatus.OK)
  public List<ShopOutputDto> getByUser(@PathVariable(name = "userIdentifier") String userIdentifier,
      @RequestHeader(name = "key", required = true) String key) {
    return shopService.getByUser(userIdentifier, key);
  }

  @GetMapping("/shopByDate")
  @ResponseStatus(HttpStatus.OK)
  public List<ShopOutputDto> getByDate(
      @RequestParam(name = "after", required = true) 
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = { "yyyy-MM-dd" }) 
      final LocalDateTime date
  ) {
    return shopService.getByDate(date.toLocalDate());
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ShopOutputDto getById(@PathVariable(name = "id") Long id) {
    return shopService.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ShopOutputDto createShop(@Valid @RequestBody ShopInputDto shopDto,
      @RequestHeader(name = "key", required = true) String key) {
    return shopService.save(shopDto, key);
  }
}
