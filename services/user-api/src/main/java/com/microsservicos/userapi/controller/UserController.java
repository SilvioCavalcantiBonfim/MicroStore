package com.microsservicos.userapi.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.microsservicos.userapi.dto.UserDto;

import jakarta.annotation.PostConstruct;

@RestController
public class UserController {

  private static List<UserDto> users = new ArrayList<>();

  @PostConstruct
  public void initUserList() {
    users.add(new UserDto("Eduardo", "123", "Rua a", "eduardo@email.com", "1234-3454", LocalDateTime.now()));
    users.add(new UserDto("Luiz", "456", "Rua b", "luiz@email.com", "1234-3454", LocalDateTime.now()));
    users.add(new UserDto("Bruna", "678", "Rua c", "bruna@email.com", "1234-3454", LocalDateTime.now()));
  }

  @GetMapping("/users")
  public List<UserDto> getAllUsers() {
    return users;
  }

  @GetMapping("/users/{cpf}")
  public UserDto getUserForCPF(@PathVariable String cpf) {
    for (UserDto user : users) {
      if (user.cpf().equals(cpf)) {
        return user;
      }
    }
    return null;
  }

  @PostMapping("/users")
  public UserDto createUser(@RequestBody UserDto user) {
    users.add(new UserDto(user.name(), user.cpf(), user.address(), user.email(), user.phone(), LocalDateTime.now()));
    return user;
  }

  @DeleteMapping("/users/{cpf}")
  public boolean deleteUserForCPF(@PathVariable String cpf) {
    for (UserDto user : users) {
      if (user.cpf().equals(cpf)) {
        users.remove(user);
        return true;
      }
    }
    return false;
  }
}
