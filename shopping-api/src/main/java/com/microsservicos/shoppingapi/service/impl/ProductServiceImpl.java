package com.microsservicos.shoppingapi.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import com.microsservicos.dto.ProductDto;
import com.microsservicos.exception.ProductNotFoundException;
import com.microsservicos.shoppingapi.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

  private final RestClient productClient;

  public ProductServiceImpl(@Qualifier("productClient") RestClient productClient) {
    this.productClient = productClient;
  }

  @Override
  public ProductDto retrieveProductByIdentifier(String productIdentifier) {
    try {
      return fetchProductDetails(productIdentifier);
    } catch (RestClientResponseException e){
      if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
        throw new ProductNotFoundException();
      }
      throw new RuntimeException();
    }
  }

  private ProductDto fetchProductDetails(String productIdentifier) {
    return productClient.get().uri(String.format("/product/%s", productIdentifier)).accept(MediaType.APPLICATION_JSON)
        .retrieve().body(ProductDto.class);
  }

}
