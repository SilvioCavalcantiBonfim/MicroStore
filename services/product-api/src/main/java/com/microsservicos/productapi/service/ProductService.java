package com.microsservicos.productapi.service;

import java.util.List;

import com.microsservicos.productapi.dto.ProductDto;

public interface ProductService {
  List<ProductDto> getAll();
  List<ProductDto> getProductByCategoryId(Long id);
  ProductDto findByProductIdentifier(String productIdentifier);
  ProductDto save(ProductDto productDto);
  void delete(Long id);
}
