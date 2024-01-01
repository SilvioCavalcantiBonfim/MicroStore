package com.microsservicos.shoppingapi.service;

import com.microsservicos.dto.UserOutputDto;

public interface UserService {
  UserOutputDto getUserByCpf(String cpf, String key);
}
