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
  public List<UserOutputDto> getAllUsers() {
    List<User> all = userRepository.findAll();
    return all.stream().map(Mapper::UserToUserOutputDto).map(UserOutputDto::obfuscate).toList();
  }

  @Override
  public UserOutputDto findUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
    return Mapper.UserToUserOutputDto(user).obfuscate();
  }

  @Override
  public UserOutputDto createUser(UserInputDto userDto) {
    if (userRepository.existsByCpf(userDto.cpf())) {
      throw new CpfAlreadyRegisteredException();
    }

    User userSaved = Mapper.UserInputDtoToUser(userDto);

    userSaved.setKey(UUID.randomUUID().toString());

    userSaved.setRegister(LocalDateTime.now());
    
    User user = userRepository.save(userSaved);
    
    return Mapper.UserToUserOutputDto(user);
  }

  @Override
  public UserOutputDto findUserByCpf(String cpf, String key) {
    if (cpf.length() != 11) {
      throw new InvalidCpfLengthException();
    }
    User user = userRepository.findByCpfAndKey(cpf, key).orElseThrow(() -> new UserNotFoundException());
    return Mapper.UserToUserOutputDto(user).obfuscate();
  }
  
  @Override
  public List<UserOutputDto> findUsersByName(String name) {
    return userRepository.queryByNameLike(name).stream().map(Mapper::UserToUserOutputDto).map(UserOutputDto::obfuscate).toList();
  }

  @Override
  public void deleteUser(long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
    userRepository.delete(user);
  }
}
