package com.microsservicos.userapi.model;

import java.time.LocalDateTime;
import com.microsservicos.userapi.dto.UserDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;

  private String cpf;

  private String address;

  private String email;

  private String phone;
  
  private LocalDateTime register;

  public static User convert(UserDto userDto){
    User user = new User();
    user.setName(userDto.name());
    user.setAddress(userDto.address());
    user.setCpf(userDto.cpf());
    user.setEmail(userDto.email());
    user.setPhone(userDto.phone());
    user.setRegister(userDto.register());

    return user;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public LocalDateTime getRegister() {
    return register;
  }

  public void setRegister(LocalDateTime register) {
    this.register = register;
  }
  
}