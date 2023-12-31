package com.microsservicos.dto.user;

import java.time.LocalDateTime;

public record UserOutputDto(
    String name,
    String cpf,
    String address,
    String email,
    String phone,
    LocalDateTime register,
    String key) {

    public UserOutputDto obfuscate(){
      return new UserOutputDto(name, cpf, address, email, phone, register, key.replaceAll("[0-9a-fA-F]", "*"));
    }
}