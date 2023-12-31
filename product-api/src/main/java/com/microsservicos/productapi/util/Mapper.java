package com.microsservicos.productapi.util;

import java.util.Objects;

import com.microsservicos.dto.product.CategoryDto;
import com.microsservicos.dto.product.ProductDto;
import com.microsservicos.productapi.model.Category;
import com.microsservicos.productapi.model.Product;

public final class Mapper {
  private Mapper() {
  }

  public static CategoryDto categoryToCategoryDto(Category category) {
    if (category == null) {
      return null;
    }
    return new CategoryDto(category.getId(), category.getName());
  }

  public static ProductDto productToProductDto(Product product) {
    if (product == null) {
      return null;
    }
    return new ProductDto(
        product.getProductIdentifier(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        Mapper.categoryToCategoryDto(product.getCategory()));
  }

  public static Product productDtoToProduct(ProductDto productDto) {
    if (productDto == null) {
      return null;
    }

    Product product = new Product();

    product.setProductIdentifier(productDto.productIdentifier());
    product.setName(productDto.name());
    product.setDescription(productDto.description());
    product.setPrice(productDto.price());
    
    
    if (Objects.nonNull(productDto.category())) {
      Category category = new Category();
      category.setId(productDto.category().id());
      category.setName(productDto.category().name());

      product.setCategory(category);
    }

    return product;
  }
}
