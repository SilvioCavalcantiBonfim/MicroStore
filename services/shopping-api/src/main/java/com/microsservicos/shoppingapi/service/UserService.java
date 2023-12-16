package com.microsservicos.shoppingapi.service;

import java.util.List;

import com.microsservicos.dto.UserDto;

public interface UserService {
  List<UserDto> getUserByCpf(String cpf);
}
