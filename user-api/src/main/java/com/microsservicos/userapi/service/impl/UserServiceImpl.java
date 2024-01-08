package com.microsservicos.userapi.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.microsservicos.dto.UserInputDto;
import com.microsservicos.dto.UserOutputDto;
import com.microsservicos.exception.CpfAlreadyRegisteredException;
import com.microsservicos.exception.InvalidCpfLengthException;
import com.microsservicos.exception.UserNotFoundException;
import com.microsservicos.userapi.model.User;
import com.microsservicos.userapi.repository.UserRepository;
import com.microsservicos.userapi.service.UserService;
import com.microsservicos.userapi.util.Mapper;

@Service
public class UserServiceImpl implements UserService {
  
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  @Override
  public List<UserOutputDto> retrieveAllUsers() {
    List<User> all = userRepository.findAll();
    return all.stream().map(Mapper::convertUserToDto).map(UserOutputDto::obfuscate).toList();
  }

  @Override
  public UserOutputDto retrieveUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
    return Mapper.convertUserToDto(user).obfuscate();
  }

  @Override
  public UserOutputDto registerNewUser(UserInputDto userDto) {
    if (userRepository.existsByCpf(userDto.cpf())) {
      throw new CpfAlreadyRegisteredException();
    }

    User userSaved = Mapper.convertDtoToUser(userDto);

    userSaved.setKey(UUID.randomUUID().toString());

    userSaved.setRegister(LocalDateTime.now());
    
    User user = userRepository.save(userSaved);
    
    return Mapper.convertUserToDto(user);
  }

  @Override
  public UserOutputDto retrieveUserByCpf(String cpf, String key) {
    if (cpf.length() != 11) {
      throw new InvalidCpfLengthException();
    }
    User user = userRepository.findByCpfAndKey(cpf, key).orElseThrow(() -> new UserNotFoundException());
    return Mapper.convertUserToDto(user).obfuscate();
  }
  
  @Override
  public List<UserOutputDto> retrieveUsersByName(String name) {
    return userRepository.queryByNameLike(name).stream().map(Mapper::convertUserToDto).map(UserOutputDto::obfuscate).toList();
  }

  @Override
  public void removeUser(long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
    userRepository.delete(user);
  }
}
