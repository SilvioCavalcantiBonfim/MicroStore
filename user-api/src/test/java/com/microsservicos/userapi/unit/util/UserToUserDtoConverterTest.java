package com.microsservicos.userapi.unit.util;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.microsservicos.dto.UserInputDto;
import com.microsservicos.dto.UserOutputDto;
import com.microsservicos.userapi.model.User;
import com.microsservicos.userapi.util.Mapper;

public class UserToUserDtoConverterTest {

  @Test
  public void convertUserToUserDtoTest(){ 
    LocalDateTime cadastro = LocalDateTime.now();
    
    UserOutputDto expected = new UserOutputDto("marcela","123","Rua abc","marcela@email.com","1234-3454", cadastro, "123");
  
    User user = new User();
    user.setName("marcela");
    user.setCpf("123");
    user.setAddress("Rua abc");
    user.setEmail("marcela@email.com");
    user.setPhone("1234-3454");
    user.setRegister(cadastro);
    user.setKey("123");
  
    UserOutputDto result = Mapper.convertUserToDto(user);
    
    Assertions.assertThat(expected).isEqualTo(result);
  }

  @Test
  public void convertUserInputToUserTest(){ 
    
    UserInputDto user = new UserInputDto("marcela","123","Rua abc","marcela@email.com","1234-3454");
  
    User expected = new User();
    expected.setName("marcela");
    expected.setCpf("123");
    expected.setAddress("Rua abc");
    expected.setEmail("marcela@email.com");
    expected.setPhone("1234-3454");
  
    User result = Mapper.convertDtoToUser(user);
    
    Assertions.assertThat(expected).usingRecursiveComparison().isEqualTo(result);
  }
      
  
}
