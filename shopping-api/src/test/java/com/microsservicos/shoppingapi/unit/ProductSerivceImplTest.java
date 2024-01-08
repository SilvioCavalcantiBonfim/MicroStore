package com.microsservicos.shoppingapi.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import com.microsservicos.dto.CategoryDto;
import com.microsservicos.dto.ProductDto;
import com.microsservicos.exception.ProductNotFoundException;
import com.microsservicos.shoppingapi.service.impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductSerivceImplTest {
  
  @Mock
  private RestClient productClient;
  
  @Mock
  @SuppressWarnings("rawtypes")
  private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
  
  @Mock
  @SuppressWarnings("rawtypes")
  private RestClient.RequestHeadersSpec requestHeadersSpec;
  
  @Mock
  private RestClient.ResponseSpec responseSpec;
  
  @InjectMocks
  private ProductServiceImpl productService;

  @BeforeEach
  private void setup() {
    Mockito.when(productClient.get()).thenReturn(requestHeadersUriSpec);
    Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
    Mockito.when(requestHeadersSpec.accept(Mockito.any())).thenReturn(requestHeadersSpec);
    Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
  }

  @Test
  public void getProductByIdentifierExceptionNotFound() {
    Mockito.when(responseSpec.body(ProductDto.class)).thenThrow(new RestClientResponseException(
        "not found", HttpStatus.NOT_FOUND, null, null, null, null));

    assertThatExceptionOfType(ProductNotFoundException.class).isThrownBy(() -> productService.retrieveProductByIdentifier("ABC123"));
  }

  @Test
  public void getProductByIdentifierExceptionOtherError() {
    Mockito.when(responseSpec.body(ProductDto.class)).thenThrow(new RestClientResponseException(
      "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, null, null, null, null));

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> productService.retrieveProductByIdentifier("ABC123"));
  }

  @Test
  public void getProductByIdentifierSuccess() {

    ProductDto expected = new ProductDto("ABC123", "product test", "description", 5.5f, new CategoryDto(1L, "test"));

    Mockito.when(responseSpec.body(ProductDto.class)).thenReturn(expected);

    ProductDto result = productService.retrieveProductByIdentifier("ABC123");

    assertThat(result).isEqualTo(expected);
  }
}
