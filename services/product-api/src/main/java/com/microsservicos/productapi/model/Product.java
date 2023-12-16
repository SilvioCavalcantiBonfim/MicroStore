package com.microsservicos.productapi.model;

import com.microsservicos.dto.ProductDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "product")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private String productIdentifier;
  
  private String name;
  
  private String description;
  
  private Float price;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProductIdentifier() {
    return productIdentifier;
  }

  public void setProductIdentifier(String productIdentifier) {
    this.productIdentifier = productIdentifier;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public static Product convert(ProductDto productDto){
    Product product = new Product();
    if (productDto.category() != null) {
      product.setCategory(Category.convert(productDto.category()));
    }
    product.setProductIdentifier(productDto.productIdentifier());
    product.setName(productDto.name());
    product.setDescription(productDto.description());
    product.setPrice(productDto.price());
    return product;
  }
}
