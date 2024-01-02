package com.microsservicos.productapi.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;

import com.microsservicos.dto.ProductDto;
import com.microsservicos.exception.CategoryNotFoundException;
import com.microsservicos.exception.ProductNotFoundException;
import com.microsservicos.productapi.model.Product;
import com.microsservicos.productapi.repository.CategoryRepository;
import com.microsservicos.productapi.repository.ProductRepository;
import com.microsservicos.productapi.service.ProductService;
import com.microsservicos.productapi.util.Mapper;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository){
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<ProductDto> getAllProducts() {
    return productRepository.findAll().stream().map(Mapper::productToProductDto).toList();
  }

  @Override
  public List<ProductDto> findProductsByCategoryId(Long id) {
    if (!categoryRepository.existsById(id)) {
      throw new CategoryNotFoundException();
    }
    return productRepository.getByCategory(id).stream().map(Mapper::productToProductDto).toList();
  }

  @Override
  public ProductDto findByProductIdentifier(String productIdentifier) {
    Product product = productRepository.findByProductIdentifier(productIdentifier).orElseThrow(() -> new ProductNotFoundException());
    return Mapper.productToProductDto(product);
  }

  @Override
  public ProductDto createProduct(ProductDto productDto) {
    if (!categoryRepository.existsById(productDto.category().id())) {
      throw new CategoryNotFoundException();
    }
    Product product = productRepository.save(Mapper.productDtoToProduct(productDto));
    return Mapper.productToProductDto(product);
  }

  @Override
  public void deleteProduct(String productIdentifier) {
    Product product = productRepository.findByProductIdentifier(productIdentifier).orElseThrow(() -> new ProductNotFoundException());
    productRepository.delete(product);
  }
}
