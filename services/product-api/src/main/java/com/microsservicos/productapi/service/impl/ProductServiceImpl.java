package com.microsservicos.productapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.microsservicos.productapi.dto.ProductDto;
import com.microsservicos.productapi.model.Product;
import com.microsservicos.productapi.repository.ProductRepository;
import com.microsservicos.productapi.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository){
    this.productRepository = productRepository;
  }

  @Override
  public List<ProductDto> getAll() {
    return productRepository.findAll().stream().map(ProductDto::convert).toList();
  }

  @Override
  public List<ProductDto> getProductByCategoryId(Long id) {
    return productRepository.getByCategory(id).stream().map(ProductDto::convert).toList();
  }

  @Override
  public ProductDto findByProductIdentifier(String productIdentifier) {
    Product product = productRepository.findByProductIdentifier(productIdentifier);
    if (product != null) {
      return ProductDto.convert(product);
    }
    return null;
  }

  @Override
  public ProductDto save(ProductDto productDto) {
    Product product = productRepository.save(Product.convert(productDto));
    return ProductDto.convert(product);
  }

  @Override
  public void delete(Long id) {
    Optional<Product> product = productRepository.findById(id);
    if (product.isPresent()) {
      productRepository.delete(product.get());
    }
  }
}
