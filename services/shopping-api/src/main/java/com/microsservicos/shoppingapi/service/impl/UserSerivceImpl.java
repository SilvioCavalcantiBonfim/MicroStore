package com.microsservicos.shoppingapi.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.microsservicos.dto.UserDto;
import com.microsservicos.exception.UserNotFoundException;
import com.microsservicos.shoppingapi.service.UserService;

@Service
public class UserSerivceImpl implements UserService {

  @Override
  public UserDto getUserByCpf(String cpf) {

    try {
      RestTemplate restTemplate = new RestTemplate();
      String url = String.format("http://localhost:8080/user/cpf/%s", cpf);
      ResponseEntity<UserDto> response = restTemplate.getForEntity( url, UserDto.class);
      return response.getBody();
    } catch (HttpClientErrorException.NotFound | HttpClientErrorException.UnprocessableEntity e) {
      throw new UserNotFoundException();
    }
    
  }

}
