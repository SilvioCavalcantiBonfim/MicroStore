package com.microsservicos.productapi.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;

import com.microsservicos.dto.ProductDto;
import com.microsservicos.exception.CategoryNotFoundException;
import com.microsservicos.exception.ProductNotFoundException;
import com.microsservicos.productapi.model.Category;
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
  public List<ProductDto> retrieveAllProducts() {
    return productRepository.findAll().stream().map(Mapper::convertProductToDto).toList();
  }

  @Override
  public List<ProductDto> retrieveProductsByCategory(Long id) {
    if (!categoryRepository.existsById(id)) {
      throw new CategoryNotFoundException();
    }
    return productRepository.getByCategory(id).stream().map(Mapper::convertProductToDto).toList();
  }

  @Override
  public ProductDto retrieveProductByIdentifier(String productIdentifier) {
    Product product = productRepository.findByProductIdentifier(productIdentifier).orElseThrow(() -> new ProductNotFoundException());
    return Mapper.convertProductToDto(product);
  }

  @Override
  public ProductDto addProduct(ProductDto productDto) {
    Category category = categoryRepository.findById(productDto.category().id()).orElseThrow(() -> new CategoryNotFoundException());
    Product product = productRepository.save(Mapper.convertDtoToProduct(productDto));
    product.setCategory(category);
    return Mapper.convertProductToDto(product);
  }

  @Override
  public void removeProductByIdentifier(String productIdentifier) {
    Product product = productRepository.findByProductIdentifier(productIdentifier).orElseThrow(() -> new ProductNotFoundException());
    productRepository.delete(product);
  }
}
