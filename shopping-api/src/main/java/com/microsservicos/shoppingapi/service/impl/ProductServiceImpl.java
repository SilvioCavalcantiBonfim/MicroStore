package com.microsservicos.shoppingapi.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.microsservicos.dto.product.ProductDto;
import com.microsservicos.exception.ProductNotFoundException;
import com.microsservicos.shoppingapi.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

  private final String productUrl;
  private final RestTemplate restTemplate;

  public ProductServiceImpl(
      @Value("${PRODUCT_API_URL:http://localhost:8081/product}") String productUrl,
      RestTemplate restTemplate) {
    this.productUrl = productUrl;
    this.restTemplate = restTemplate;
  }

  @Override
  public ProductDto getProductByIdentifier(String productIdentifier) {
    try {
      return fetchProductResponse(productIdentifier);
    } catch (HttpClientErrorException.NotFound e) {
      throw new ProductNotFoundException();
    } catch (RestClientException e){
      throw new RuntimeException();
    }
  }

  private ProductDto fetchProductResponse(String productIdentifier) {
    String url = buildProductUrl(productIdentifier);
    ResponseEntity<ProductDto> response = restTemplate.getForEntity(url, ProductDto.class);
    return response.getBody();
  }

  private String buildProductUrl(String productIdentifier) {
    return String.format("%s/%s", productUrl, productIdentifier);
  }

}
