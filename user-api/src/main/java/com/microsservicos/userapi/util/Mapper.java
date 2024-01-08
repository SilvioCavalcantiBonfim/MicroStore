package com.microsservicos.userapi.util;

import com.microsservicos.dto.UserInputDto;
import com.microsservicos.dto.UserOutputDto;
import com.microsservicos.userapi.model.User;

public final class Mapper {

  private Mapper(){}

  public static UserOutputDto convertUserToDto(User user) {
    UserOutputDto userDto = new UserOutputDto(user.getName(), user.getCpf(), user.getAddress(), user.getEmail(), user.getPhone(),
        user.getRegister(), user.getKey());
    return userDto;
  }

  public static User convertDtoToUser(UserInputDto userInput) {
    User user = new User();
    user.setName(userInput.name());
    user.setAddress(userInput.address());
    user.setCpf(userInput.cpf());
    user.setEmail(userInput.email());
    user.setPhone(userInput.phone());
    return user;
  }
}
