package com.microsservicos.userapi.dto;


import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.microsservicos.userapi.model.User;

public class UserDtoTest {
  @Test
  void testConvert() {
    
    LocalDateTime cadastro = LocalDateTime.now();
    
    UserDto expected = new UserDto("marcela","123","Rua abc","marcela@email.com","1234-3454", cadastro);

    User user = new User();
    user.setName("marcela");
    user.setCpf("123");
    user.setAddress("Rua abc");
    user.setEmail("marcela@email.com");
    user.setPhone("1234-3454");
    user.setRegister(cadastro);

    Assertions.assertEquals(expected, UserDto.convert(user));

  }
}
