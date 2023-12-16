package com.microsservicos.userapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microsservicos.dto.UserDto;
import com.microsservicos.userapi.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  
  public UserController(UserService userService){
    this.userService = userService;
  }

  @GetMapping
  public List<UserDto> getAllUsers() {
    return userService.getAll();
  }

  @GetMapping("/{id}")
  public UserDto getUserForId(@PathVariable long id) {
    return userService.findById(id);
  }

  @PostMapping
  public UserDto createUser(@RequestBody UserDto user) {
    return userService.save(user);
  }

  @GetMapping("/cpf/{cpf}")
  public List<UserDto> getUserForCPF(@PathVariable String cpf) {
    return userService.findByCpf(cpf);
  }

  @DeleteMapping("/{id}")
  public boolean deleteUserForCPF(@PathVariable long id) {
    return userService.deleteUser(id);
  }

  @GetMapping("/search")
  public List<UserDto> getUserForLikeName(@RequestParam(name = "name", required = true) String name) {
    return userService.queryByName(name);
  }
}
