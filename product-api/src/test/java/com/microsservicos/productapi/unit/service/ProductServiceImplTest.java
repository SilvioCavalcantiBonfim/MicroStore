package com.microsservicos.productapi.unit.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.List;
import java.util.Optional;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microsservicos.dto.CategoryDto;
import com.microsservicos.dto.ProductDto;
import com.microsservicos.exception.CategoryNotFoundException;
import com.microsservicos.exception.ProductNotFoundException;
import com.microsservicos.productapi.model.*;
import com.microsservicos.productapi.repository.CategoryRepository;
import com.microsservicos.productapi.repository.ProductRepository;
import com.microsservicos.productapi.service.impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private ProductServiceImpl productService;

  private static Category category1 = new Category();
  private static CategoryDto category1Dto = new CategoryDto(1L, "category 1");
  private static Category category2 = new Category();
  private static CategoryDto category2Dto = new CategoryDto(2L, "category 2");

  private static Product product1 = new Product();
  private static Product product2 = new Product();
  private static ProductDto product1Dto = new ProductDto("product1", "Product 1", "description", 10.0f, category1Dto);
  
  @BeforeAll
  private static void setup() {

    category1.setId(1L);
    category1.setName("category 1");

    category2.setId(2L);
    category2.setName("category 2");

    product1.setId(1L);
    product1.setName("Product 1");
    product1.setDescription("description");
    product1.setCategory(category1);
    product1.setPrice(10.0f);
    product1.setProductIdentifier("product1");

    product2.setId(2L);
    product2.setName("Product 2");
    product2.setDescription("description");
    product2.setCategory(category2);
    product2.setPrice(10.0f);
    product2.setProductIdentifier("product2");
  }

  @Test
  public void findAllProductSuccessTest() {

    when(productRepository.findAll()).thenReturn(List.of(product1, product2));

    List<ProductDto> output = productService.getAllProducts();

    assertThat(output)
        .extracting("productIdentifier", "name", "description", "price", "category")
        .contains(
            new Tuple("product1", "Product 1", "description", 10.0f, category1Dto),
            new Tuple("product2", "Product 2", "description", 10.0f, category2Dto));
  }

  @Test
  public void findByCategorySuccessTest() {

    when(categoryRepository.existsById(any())).thenReturn(true);
    when(productRepository.getByCategory(any())).thenReturn(List.of(product1));

    List<ProductDto> output = productService.findProductsByCategoryId(1L);

    assertThat(output)
        .extracting("productIdentifier", "name", "description", "price", "category")
        .contains(
            new Tuple("product1", "Product 1", "description", 10.0f, category1Dto));
  }

  @Test
  public void findByCategoryExceptionTest() {

    when(categoryRepository.existsById(any())).thenReturn(false);

    assertThatExceptionOfType(CategoryNotFoundException.class).isThrownBy(() -> productService.findProductsByCategoryId(1L));
  }

  @Test
  public void findByProductIdentifierSuccessTest() {

    when(productRepository.findByProductIdentifier(any())).thenReturn(Optional.of(product1));

    ProductDto output = productService.findByProductIdentifier("product1");

    assertThat(output).isEqualTo(product1Dto);
  }

  @Test
  public void findByProductIdentifierExceptionTest() {

    when(productRepository.findByProductIdentifier(any())).thenReturn(Optional.empty());

    assertThatExceptionOfType(ProductNotFoundException.class).isThrownBy(() -> productService.findByProductIdentifier("product1"));
  }

  @Test
  public void deleteSuccessTest() {

    when(productRepository.findByProductIdentifier(any())).thenReturn(Optional.of(product1));

    assertThatNoException().isThrownBy(() -> productService.deleteProduct("product1"));
  }

  @Test
  public void deleteExceptionTest() {

    when(productRepository.findByProductIdentifier(any())).thenReturn(Optional.empty());

    assertThatExceptionOfType(ProductNotFoundException.class).isThrownBy(() -> productService.deleteProduct("product1"));
  }

  @Test
  public void saveSuccessTest() {

    when(categoryRepository.existsById(any())).thenReturn(true);
    when(productRepository.save(any())).thenReturn(product1);

    ProductDto output = productService.createProduct(product1Dto);

    assertThat(output).isEqualTo(product1Dto);
  }

  @Test
  public void saveExceptionTest() {

    when(categoryRepository.existsById(any())).thenReturn(false);

    assertThatExceptionOfType(CategoryNotFoundException.class).isThrownBy(() -> productService.createProduct(product1Dto));
  }
}
