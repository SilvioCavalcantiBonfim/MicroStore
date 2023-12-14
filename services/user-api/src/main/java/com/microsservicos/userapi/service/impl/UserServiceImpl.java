package com.microsservicos.userapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.microsservicos.userapi.dto.UserDto;
import com.microsservicos.userapi.model.User;
import com.microsservicos.userapi.repository.UserRepository;
import com.microsservicos.userapi.service.UserService;

@Service
public class UserServiceImpl implements UserService {
  
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  @Override
  public List<UserDto> getAll() {
    List<User> all = userRepository.findAll();
    return all.stream().map(UserDto::convert).toList();
  }

  @Override
  public UserDto findById(Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      return UserDto.convert(user.get());
    }
    return null;
  }

  @Override
  public UserDto save(UserDto userDto) {
    User user = userRepository.save(User.convert(userDto));
    return UserDto.convert(user);
  }

  @Override
  public UserDto findByCpf(String cpf) {
    return UserDto.convert(userRepository.findByCpf(cpf));
  }
  
  @Override
  public List<UserDto> queryByName(String name) {
    return userRepository.queryByNameLike(name).stream().map(UserDto::convert).toList();
  }

  @Override
  public boolean deleteUser(long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      userRepository.delete(user.get());
      return true;
    }
    return false;
  }
}
