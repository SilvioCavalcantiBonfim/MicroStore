package com.microsservicos.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserInputDto(
    @NotBlank(message = Message.NAME_BLANK) @Pattern(regexp = "^.{0,100}", message = Message.NAME_LENGTH) String name,
    @NotBlank(message = Message.CPF_BLANK) @Pattern(regexp = "^\\d{11}$", message = Message.CPF_LENGTH) String cpf,
    @NotBlank(message = Message.ADDRESS_BLANK) String address,
    @NotBlank(message = Message.EMAIL_BLANK) @Email(message = Message.INVALID_EMAIL) String email,
    @NotBlank(message = Message.PHONE_BLANK) String phone) {
}