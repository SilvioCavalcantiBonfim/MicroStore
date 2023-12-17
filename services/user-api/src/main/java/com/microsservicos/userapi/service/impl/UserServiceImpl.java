package com.microsservicos.userapi.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.microsservicos.dto.UserDto;
import com.microsservicos.exception.CpfAlreadyRegisteredException;
import com.microsservicos.exception.InvalidCpfLengthException;
import com.microsservicos.exception.UserNotFoundException;
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
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
    return UserToUserDtoConverter.convert(user);
  }

  @Override
  public UserDto save(UserDto userDto) {
    if (userDto.cpf().length() != 11) {
      throw new InvalidCpfLengthException();
    }

    if (userRepository.existsByCpf(userDto.cpf())) {
      throw new CpfAlreadyRegisteredException();
    }

    User userSaved = User.convert(userDto);

    userSaved.setKey(UUID.randomUUID().toString());
    
    User user = userRepository.save(userSaved);
    
    return UserToUserDtoConverter.convert(user);
  }

  @Override
  public UserDto findByCpf(String cpf, String key) {
    if (cpf.length() != 11) {
      throw new InvalidCpfLengthException();
    }
    User user = userRepository.findByCpfAndKey(cpf, key).orElseThrow(() -> new UserNotFoundException());
    return UserToUserDtoConverter.convert(user);
  }
  
  @Override
  public List<UserDto> queryByName(String name) {
    return userRepository.queryByNameLike(name).stream().map(UserToUserDtoConverter::convert).toList();
  }

  @Override
  public void deleteUser(long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
    userRepository.delete(user);
  }
}
