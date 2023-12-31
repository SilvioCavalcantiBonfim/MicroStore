package com.microsservicos.productapi.service;

import java.util.List;

import com.microsservicos.dto.product.ProductDto;

public interface ProductService {
  List<ProductDto> getAllProducts();
  List<ProductDto> findProductsByCategoryId(Long id);
  ProductDto findByProductIdentifier(String productIdentifier);
  ProductDto createProduct(ProductDto productDto);
  void deleteProduct(String id);
}
