package com.microsservicos.shoppingapi.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.microsservicos.dto.UserDto;
import com.microsservicos.exception.UserNotFoundException;
import com.microsservicos.shoppingapi.service.UserService;

@Service
public class UserSerivceImpl implements UserService {

  private final String userUrl;
  private final RestTemplate restTemplate;

  public UserSerivceImpl(
    @Value("${USER_API_URL:http://localhost:8080/user}") String userUrl,
    RestTemplate restTemplate
  ) {
    this.userUrl = userUrl;
    this.restTemplate = restTemplate;
  }

  @Override
  public UserDto getUserByCpf(String cpf, String key) {
    try {
      return fetchUserByCpfAndKey(cpf, key);
    } catch (HttpClientErrorException.NotFound | HttpClientErrorException.UnprocessableEntity e) {
      throw new UserNotFoundException();
    }

  }

  private UserDto fetchUserByCpfAndKey(String cpf, String key) {
    String url = buildUserUrlWithCpfAndKey(cpf, key);
    ResponseEntity<UserDto> response = restTemplate.getForEntity(url, UserDto.class);
    return response.getBody();
  }

  private String buildUserUrlWithCpfAndKey(String cpf, String key) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(String.format("%s/cpf/%s", userUrl, cpf));
    builder.queryParam("key", key);
    return builder.toUriString();
  }

}
