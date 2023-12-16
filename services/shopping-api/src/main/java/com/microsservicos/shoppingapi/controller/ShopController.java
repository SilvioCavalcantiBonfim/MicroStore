package com.microsservicos.shoppingapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microsservicos.dto.ShopDto;
import com.microsservicos.shoppingapi.service.ShopService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/shopping")
public class ShopController {
  
  private final ShopService shopService;

  public ShopController(ShopService shopService){
    this.shopService = shopService;
  }

  @GetMapping
  public List<ShopDto> getAll(){
    return shopService.getAll();
  }

  @GetMapping("/shopByUser/{userIdentifier}")
  public List<ShopDto> getByUser(@PathVariable String userIdentifier){
    return shopService.getByUser(userIdentifier);
  }

  @GetMapping("/shopByDate")
  public List<ShopDto> getByDate(@RequestBody ShopDto shopDto){
    return shopService.getByDate(shopDto);
  }

  @GetMapping("/{id}")
  public ShopDto getById(@PathVariable Long id){
    return shopService.findById(id);
  }

  @PostMapping
  public ShopDto createShop(@Valid @RequestBody ShopDto shopDto){
    return shopService.save(shopDto);
  }
}
