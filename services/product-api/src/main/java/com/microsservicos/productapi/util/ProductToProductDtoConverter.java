package com.microsservicos.productapi.util;

import com.microsservicos.dto.ProductDto;
import com.microsservicos.productapi.model.Product;

public class ProductToProductDtoConverter{

  public static ProductDto convert(Product product) {
    if (product == null) {
      return null;
    }
    return new ProductDto(
        product.getProductIdentifier(),
        product.getProductIdentifier(),
        product.getDescription(),
        product.getPrice(),
        CategoryToCategoryDtoConverter.convert(product.getCategory()));
  }

}
