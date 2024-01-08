package com.microsservicos.shoppingapi.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.time.LocalDateTime;

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
import com.microsservicos.dto.UserOutputDto;
import com.microsservicos.exception.InvalidCpfLengthException;
import com.microsservicos.exception.UserNotFoundException;
import com.microsservicos.shoppingapi.service.impl.UserSerivceImpl;

@ExtendWith(MockitoExtension.class)
public class UserSerivceImplTest {
  
  @Mock
  private RestClient userClient;

  @Mock
  @SuppressWarnings("rawtypes")
  private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

  @Mock
  @SuppressWarnings("rawtypes")
  private RestClient.RequestHeadersSpec requestHeadersSpec;

  @Mock
  private RestClient.ResponseSpec responseSpec;

  @InjectMocks
  private UserSerivceImpl userSerivce;

  @BeforeEach
  private void setup() {
    Mockito.when(userClient.get()).thenReturn(requestHeadersUriSpec);
    Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
    Mockito.when(requestHeadersSpec.header(Mockito.any(),Mockito.any())).thenReturn(requestHeadersSpec);
    Mockito.when(requestHeadersSpec.accept(Mockito.any())).thenReturn(requestHeadersSpec);
    Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
  }

  @Test
  public void getUserByCpfExceptionNotFound() {
    Mockito.when(responseSpec.body(UserOutputDto.class)).thenThrow(new RestClientResponseException(
        "not found", HttpStatus.NOT_FOUND, null, null, null, null));

    assertThatExceptionOfType(UserNotFoundException.class)
        .isThrownBy(() -> userSerivce.retrieveUserByCpf("12345678900", "test"));
  }

  @Test
  public void getUserByCpfExceptionInvalidCpfLength() {
    Mockito.when(responseSpec.body(UserOutputDto.class)).thenThrow(new RestClientResponseException(
        "unprocessable entity", HttpStatus.UNPROCESSABLE_ENTITY, null, null, null, null));

    assertThatExceptionOfType(InvalidCpfLengthException.class)
        .isThrownBy(() -> userSerivce.retrieveUserByCpf("1234567890", "test"));
  }

  @Test
  public void getUserByCpfExceptionOtherError() {
    Mockito.when(responseSpec.body(UserOutputDto.class)).thenThrow(new RestClientResponseException(
        "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, null, null, null, null));

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> userSerivce.retrieveUserByCpf("12345678900", "test"));
  }

  @Test
  public void getUserByCpfSuccess() {

    UserOutputDto expected = new UserOutputDto("user test", "12345678900", "street B", "test@email.com", "900000000",
        LocalDateTime.now(), "test");

    Mockito.when(responseSpec.body(UserOutputDto.class)).thenReturn(expected);

    UserOutputDto result = userSerivce.retrieveUserByCpf("12345678900", "test");

    assertThat(result).isEqualTo(expected);
  }
}
