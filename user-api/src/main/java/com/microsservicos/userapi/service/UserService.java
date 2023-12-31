package com.microsservicos.userapi.service;

import java.util.List;

import com.microsservicos.dto.user.UserOutputDto;
import com.microsservicos.dto.user.UserInputDto;

public interface UserService {
  
  List<UserOutputDto> getAllUsers();

  UserOutputDto findUserById(Long id);

  UserOutputDto createUser(UserInputDto userDto);

  UserOutputDto findUserByCpf(String cpf, String key);

  List<UserOutputDto> findUsersByName(String name);

  void deleteUser(long id);
}
