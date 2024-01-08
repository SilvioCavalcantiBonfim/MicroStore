package com.microsservicos.userapi.service;

import java.util.List;

import com.microsservicos.dto.UserInputDto;
import com.microsservicos.dto.UserOutputDto;

public interface UserService {
  
  List<UserOutputDto> retrieveAllUsers();

  UserOutputDto retrieveUserById(Long id);

  UserOutputDto registerNewUser(UserInputDto userDto);

  UserOutputDto retrieveUserByCpf(String cpf, String key);

  List<UserOutputDto> retrieveUsersByName(String name);

  void removeUser(long id);
}
