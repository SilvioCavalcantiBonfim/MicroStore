package com.microsservicos.shoppingapi.service;

import com.microsservicos.dto.product.ProductDto;

public interface ProductService {
  ProductDto getProductByIdentifier(String productIdentifier);
}
