package com.microsservicos.shoppingapi.service;

import com.microsservicos.dto.ProductDto;

public interface ProductService {
  ProductDto getProductByIdentifier(String productIdentifier);
}
