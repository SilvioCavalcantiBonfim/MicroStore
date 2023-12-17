package com.microsservicos.productapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.microsservicos.dto.ProductDto;
import com.microsservicos.exception.CategoryNotFoundException;
import com.microsservicos.exception.ProductNotFoundException;
import com.microsservicos.productapi.model.Product;
import com.microsservicos.productapi.repository.CategoryRepository;
import com.microsservicos.productapi.repository.ProductRepository;
import com.microsservicos.productapi.service.ProductService;
import com.microsservicos.productapi.util.ProductToProductDtoConverter;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository){
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<ProductDto> getAll() {
    return productRepository.findAll().stream().map(ProductToProductDtoConverter::convert).toList();
  }

  @Override
  public List<ProductDto> getProductByCategoryId(Long id) {
    if (!categoryRepository.existsById(id)) {
      throw new CategoryNotFoundException();
    }
    return productRepository.getByCategory(id).stream().map(ProductToProductDtoConverter::convert).toList();
  }

  @Override
  public ProductDto findByProductIdentifier(String productIdentifier) {
    Optional<Product> product = productRepository.findByProductIdentifier(productIdentifier);
    if (product.isPresent()) {
      return ProductToProductDtoConverter.convert(product.get());
    }
    throw new ProductNotFoundException();
  }

  @Override
  public ProductDto save(ProductDto productDto) {
    if (!categoryRepository.existsById(productDto.category().id())) {
      throw new CategoryNotFoundException();
    }
    Product product = productRepository.save(Product.convert(productDto));
    return ProductToProductDtoConverter.convert(product);
  }

  @Override
  public void delete(String productIdentifier) {
    Optional<Product> product = productRepository.findByProductIdentifier(productIdentifier);
    if (!product.isPresent()) {
      throw new ProductNotFoundException();
    }
    productRepository.delete(product.get());
  }
}
