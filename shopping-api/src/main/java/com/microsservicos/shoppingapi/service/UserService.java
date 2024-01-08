package com.microsservicos.shoppingapi.service;

import com.microsservicos.dto.UserOutputDto;

public interface UserService {
  UserOutputDto retrieveUserByCpf(String cpf, String key);
}
