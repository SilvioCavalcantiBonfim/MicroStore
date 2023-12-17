package com.microsservicos.userapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
  @ResponseStatus(HttpStatus.OK)
  public List<UserDto> getAllUsers() {
    return userService.getAll();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserDto getUserForId(@PathVariable long id) {
    return userService.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserDto createUser(@RequestBody UserDto user) {
    return userService.save(user);
  }

  @GetMapping("/cpf/{cpf}")
  @ResponseStatus(HttpStatus.OK)
  public UserDto getUserForCPF(@PathVariable String cpf, @RequestParam(name = "key", required = true) String key) {
    return userService.findByCpf(cpf, key);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUserForCPF(@PathVariable long id) {
    userService.deleteUser(id);
  }

  @GetMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public List<UserDto> getUserForLikeName(@RequestParam(name = "name", required = true) String name) {
    return userService.queryByName(name);
  }
}
