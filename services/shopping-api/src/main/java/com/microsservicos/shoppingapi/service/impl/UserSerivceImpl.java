package com.microsservicos.shoppingapi.service.impl;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import com.microsservicos.dto.UserDto;
import com.microsservicos.shoppingapi.service.UserService;

@Service
public class UserSerivceImpl implements UserService {

  @Override
  public List<UserDto> getUserByCpf(String cpf) {
    
    RestTemplate restTemplate = new RestTemplate();

    String url = String.format("http://localhost:8080/user/cpf/%s", cpf);
    
    ResponseEntity<List<UserDto>> response = restTemplate.exchange( url, HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDto>>() {});

    return response.getBody();
  }

}
