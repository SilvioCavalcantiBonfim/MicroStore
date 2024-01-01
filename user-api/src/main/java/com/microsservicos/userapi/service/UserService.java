package com.microsservicos.userapi.service;

import java.util.List;

import com.microsservicos.dto.UserInputDto;
import com.microsservicos.dto.UserOutputDto;

public interface UserService {
  
  List<UserOutputDto> getAllUsers();

  UserOutputDto findUserById(Long id);

  UserOutputDto createUser(UserInputDto userDto);

  UserOutputDto findUserByCpf(String cpf, String key);

  List<UserOutputDto> findUsersByName(String name);

  void deleteUser(long id);
}
