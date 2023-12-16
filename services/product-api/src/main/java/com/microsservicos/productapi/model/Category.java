package com.microsservicos.productapi.model;

import com.microsservicos.dto.CategoryDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "category")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static Category convert(CategoryDto categoryDto){
    Category category = new Category();
    if (categoryDto != null) {
      category.setId(categoryDto.id());
      category.setName(categoryDto.name());
    }
    return category;
  }
}
