package com.microsservicos.shoppingapi.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.microsservicos.dto.product.CategoryDto;
import com.microsservicos.dto.product.ProductDto;
import com.microsservicos.exception.ProductNotFoundException;
import com.microsservicos.shoppingapi.service.impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductSerivceImplTest {

  @Mock
  private RestTemplate restTemplate;

  private String url = "http://localhost:8081/";

  private ProductServiceImpl productService;

  @BeforeEach
  private void setup() {
    productService = new ProductServiceImpl(url, restTemplate);
  }

  @Test
  public void getProductByIdentifierExceptionNotFound() {
    when(restTemplate.getForEntity(anyString(), any()))
        .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", null, null, null));

    assertThatExceptionOfType(ProductNotFoundException.class).isThrownBy(() -> productService.getProductByIdentifier("ABC123"));
  }

  @Test
  public void getProductByIdentifierExceptionOtherError() {
    when(restTemplate.getForEntity(anyString(), any())).thenThrow(new RestClientException(""));

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> productService.getProductByIdentifier("ABC123"));
  }

  @Test
  public void getProductByIdentifierSuccess() {

    ProductDto expected = new ProductDto("ABC123", "product test", "description", 5.5f, new CategoryDto(1L, "test"));

    when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(expected));

    ProductDto result = productService.getProductByIdentifier("ABC123");

    assertThat(result).isEqualTo(expected);
  }
}
