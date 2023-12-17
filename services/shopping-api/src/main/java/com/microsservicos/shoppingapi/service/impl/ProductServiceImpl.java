package com.microsservicos.shoppingapi.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.microsservicos.dto.ProductDto;
import com.microsservicos.exception.ProductNotFoundException;
import com.microsservicos.shoppingapi.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

  @Override
  public ProductDto getProductByIdentifier(String productIdentifier) {

    try {
      RestTemplate restTemplate = new RestTemplate();
      String url = String.format("http://localhost:8081/product/%s", productIdentifier);
      ResponseEntity<ProductDto> response = restTemplate.getForEntity(url, ProductDto.class);
      return response.getBody();
    } catch (HttpClientErrorException.NotFound e) {
      throw new ProductNotFoundException();
    }

  }

}
