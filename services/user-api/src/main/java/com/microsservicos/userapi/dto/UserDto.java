package com.microsservicos.userapi.dto;

import java.time.LocalDateTime;

public record UserDto(String name, String cpf, String address, String email, String phone, LocalDateTime register) {}
