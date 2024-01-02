package com.microsservicos.dto;

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
      return new UserOutputDto(name, Obfuscator.obfuscate(cpf), address, email, phone, register, Obfuscator.obfuscate(key));
    }
}