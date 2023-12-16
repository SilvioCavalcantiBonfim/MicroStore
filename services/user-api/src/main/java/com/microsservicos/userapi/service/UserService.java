package com.microsservicos.userapi.service;

import java.util.List;

import com.microsservicos.dto.UserDto;

public interface UserService {
  List<UserDto> getAll();

  UserDto findById(Long id);

  UserDto save(UserDto userDto);

  List<UserDto> findByCpf(String cpf);

  List<UserDto> queryByName(String name);

  boolean deleteUser(long id);
}
