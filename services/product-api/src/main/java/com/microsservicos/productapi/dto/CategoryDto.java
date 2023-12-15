package com.microsservicos.productapi.dto;

import com.microsservicos.productapi.model.Category;

import jakarta.validation.constraints.NotNull;

public record CategoryDto(
  @NotNull long id, 
  String name) {

  public static CategoryDto convert(Category category) {
    if (category == null) {
      return null;
    }
    return new CategoryDto(category.getId(), category.getName());
  }

}
