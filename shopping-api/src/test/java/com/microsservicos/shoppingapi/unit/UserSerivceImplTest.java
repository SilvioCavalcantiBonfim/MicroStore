package com.microsservicos.shoppingapi.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

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

import com.microsservicos.dto.user.UserOutputDto;
import com.microsservicos.exception.InvalidCpfLengthException;
import com.microsservicos.exception.UserNotFoundException;
import com.microsservicos.shoppingapi.service.impl.UserSerivceImpl;

@ExtendWith(MockitoExtension.class)
public class UserSerivceImplTest {

  @Mock
  private RestTemplate restTemplate;

  private String url = "http://localhost:8081/";

  private UserSerivceImpl userSerivce;

  @BeforeEach
  private void setup() {
    userSerivce = new UserSerivceImpl(url, restTemplate);
  }

  @Test
  public void getUserByCpfExceptionNotFound() {
    when(restTemplate.getForEntity(anyString(), any()))
        .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", null, null, null));

    assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> userSerivce.getUserByCpf("12345678900", "test"));
  }

  @Test
  public void getUserByCpfExceptionInvalidCpfLength() {
    when(restTemplate.getForEntity(anyString(), any())).thenThrow(
        HttpClientErrorException.create(HttpStatus.UNPROCESSABLE_ENTITY, "unprocessable entity", null, null, null));

    assertThatExceptionOfType(InvalidCpfLengthException.class)
        .isThrownBy(() -> userSerivce.getUserByCpf("1234567890", "test"));
  }

  @Test
  public void getUserByCpfExceptionOtherError() {
    when(restTemplate.getForEntity(anyString(), any())).thenThrow(new RestClientException(""));

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> userSerivce.getUserByCpf("12345678900", "test"));
  }

  @Test
  public void getUserByCpfSuccess() {

    UserOutputDto expected = new UserOutputDto("user test", "12345678900", "street B", "test@email.com", "900000000", LocalDateTime.now(), "test");

    when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(expected));

    UserOutputDto result = userSerivce.getUserByCpf("12345678900", "test");

    assertThat(result).isEqualTo(expected);
  }
}
