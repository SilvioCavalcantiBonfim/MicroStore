package com.microsservicos.shoppingapi.service;

import com.microsservicos.dto.UserDto;

public interface UserService {
  UserDto getUserByCpf(String cpf);
}
