package com.microsservicos.userapi.util;

import com.microsservicos.dto.UserDto;
import com.microsservicos.userapi.model.User;

public class UserToUserDtoConverter {

  public static UserDto convert(User user) {
    UserDto userDto = new UserDto(user.getName(), user.getCpf(), user.getAddress(), user.getEmail(), user.getPhone(),
        user.getRegister());
    return userDto;
  }
}
