package com.microsservicos.userapi.dto;

import java.time.LocalDateTime;
import com.microsservicos.userapi.model.User;

public record UserDto(
  String name,
  String cpf,
  String address,
  String email,
  String phone,  
  LocalDateTime register){
  
  public static UserDto convert(User user){
    UserDto userDto = new UserDto(user.getName(),user.getCpf(),user.getAddress(),user.getEmail(),user.getPhone(),user.getRegister());
    return userDto;
  }
}
