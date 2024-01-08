package com.microsservicos.productapi.service;

import java.util.List;

import com.microsservicos.dto.ProductDto;

public interface ProductService {
  List<ProductDto> retrieveAllProducts();
  List<ProductDto> retrieveProductsByCategory(Long id);
  ProductDto retrieveProductByIdentifier(String productIdentifier);
  ProductDto addProduct(ProductDto productDto);
  void removeProductByIdentifier(String id);
}
