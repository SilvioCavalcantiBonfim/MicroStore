package com.microsservicos.productapi.util;

import com.microsservicos.dto.CategoryDto;
import com.microsservicos.productapi.model.Category;

import jakarta.validation.constraints.NotNull;

public record CategoryToCategoryDtoConverter(
  @NotNull long id, 
  String name) {

  public static CategoryDto convert(Category category) {
    if (category == null) {
      return null;
    }
    return new CategoryDto(category.getId(), category.getName());
  }

}
