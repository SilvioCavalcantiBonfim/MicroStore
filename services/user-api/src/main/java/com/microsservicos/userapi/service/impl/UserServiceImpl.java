package com.microsservicos.userapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.microsservicos.dto.UserDto;
import com.microsservicos.userapi.model.User;
import com.microsservicos.userapi.repository.UserRepository;
import com.microsservicos.userapi.service.UserService;
import com.microsservicos.userapi.util.UserToUserDtoConverter;

@Service
public class UserServiceImpl implements UserService {
  
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  @Override
  public List<UserDto> getAll() {
    List<User> all = userRepository.findAll();
    return all.stream().map(UserToUserDtoConverter::convert).toList();
  }

  @Override
  public UserDto findById(Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      return UserToUserDtoConverter.convert(user.get());
    }
    return null;
  }

  @Override
  public UserDto save(UserDto userDto) {
    User user = userRepository.save(User.convert(userDto));
    return UserToUserDtoConverter.convert(user);
  }

  @Override
  public List<UserDto> findByCpf(String cpf) {
    return userRepository.findByCpf(cpf).stream().map(UserToUserDtoConverter::convert).toList();
  }
  
  @Override
  public List<UserDto> queryByName(String name) {
    return userRepository.queryByNameLike(name).stream().map(UserToUserDtoConverter::convert).toList();
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
