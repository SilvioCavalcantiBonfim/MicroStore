package com.microsservicos.shoppingapi.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.http.HttpStatus;

import com.microsservicos.dto.UserOutputDto;
import com.microsservicos.exception.InvalidCpfLengthException;
import com.microsservicos.exception.UserNotFoundException;
import com.microsservicos.shoppingapi.service.UserService;

@Service
public class UserSerivceImpl implements UserService {

  private final RestClient userClient;

  public UserSerivceImpl(
      @Qualifier("userClient") RestClient userClient) {
    this.userClient = userClient;
  }

  @Override
  public UserOutputDto retrieveUserByCpf(String cpf, String key) {
    try {
      return fetchUserDetailsByCpfAndKey(cpf, key);
    } catch (RestClientResponseException e) {
      switch (e.getStatusCode()) {
        case HttpStatus.NOT_FOUND -> throw new UserNotFoundException();
        case HttpStatus.UNPROCESSABLE_ENTITY -> throw new InvalidCpfLengthException();
        default -> throw new RuntimeException(e.getMessage());
      }
    }

  }

  private UserOutputDto fetchUserDetailsByCpfAndKey(String cpf, String key) {
    UserOutputDto body = userClient.get().uri(String.format("/user/cpf/%s", cpf)).header("key", key).accept(MediaType.APPLICATION_JSON)
        .retrieve().body(UserOutputDto.class);
    return body;
  }
}
