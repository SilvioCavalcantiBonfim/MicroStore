package com.microsservicos.productapi.unit.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.microsservicos.dto.CategoryDto;
import com.microsservicos.dto.ProductDto;
import com.microsservicos.productapi.model.Category;
import com.microsservicos.productapi.model.Product;
import com.microsservicos.productapi.util.Mapper;

public class MapperConverterTest {

  @Test
  public void categoryToCategoryNull() {

    CategoryDto expected = Mapper.categoryToCategoryDto(null);

    Assertions.assertThat(expected).isNull();
  }

  @Test
  public void categoryToCategoryNotNull() {

    Category category = new Category();

    category.setId(1L);
    category.setName("test");

    CategoryDto expected = new CategoryDto(1L, "test");

    CategoryDto result = Mapper.categoryToCategoryDto(category);

    Assertions.assertThat(result).isEqualTo(expected);
  }

  @Test
  public void productToProductDtoNull() {

    ProductDto expected = Mapper.productToProductDto(null);

    Assertions.assertThat(expected).isNull();
  }

  @Test
  public void productToProductDtoNotNull() {

    Product product = new Product();

    product.setId(1L);
    product.setName("test");
    product.setProductIdentifier("test");
    product.setPrice(0.0f);
    product.setDescription("description");

    Category category = new Category();
    category.setId(1L);
    category.setName("category");

    product.setCategory(category);

    ProductDto expected = new ProductDto("test", "test", "description", 0.0f, new CategoryDto(1L, "category"));

    ProductDto result = Mapper.productToProductDto(product);

    Assertions.assertThat(result).isEqualTo(expected);
  }

  @Test
  public void productDtoToProductNull() {

    Product expected = Mapper.productDtoToProduct(null);

    Assertions.assertThat(expected).isNull();
  }

  @Test
  public void productDtoToProductNotNull() {

    Product expected = new Product();

    expected.setName("test");
    expected.setProductIdentifier("test");
    expected.setPrice(0.0f);
    expected.setDescription("description");

    Category category = new Category();
    category.setId(1L);
    category.setName("category");

    expected.setCategory(category);

    ProductDto product = new ProductDto("test", "test", "description", 0.0f, new CategoryDto(1L, "category"));

    Product result = Mapper.productDtoToProduct(product);

    Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  public void productDtoToProductWithCategoryNull() {

    Product expected = new Product();

    expected.setName("test");
    expected.setProductIdentifier("test");
    expected.setPrice(0.0f);
    expected.setDescription("description");

    ProductDto product = new ProductDto("test", "test", "description", 0.0f, null);

    Product result = Mapper.productDtoToProduct(product);

    Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }
}
