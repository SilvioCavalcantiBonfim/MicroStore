package com.microsservicos.productapi.dto;

import com.microsservicos.productapi.model.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDto(
    @NotBlank String productIdentifier,
    @NotBlank String name,
    String description,
    @NotNull Float price,
    @NotNull CategoryDto category) {

  public static ProductDto convert(Product product) {
    if (product == null) {
      return null;
    }
    return new ProductDto(
        product.getProductIdentifier(),
        product.getProductIdentifier(),
        product.getDescription(),
        product.getPrice(),
        CategoryDto.convert(product.getCategory()));
  }

}
