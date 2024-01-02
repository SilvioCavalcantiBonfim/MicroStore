package com.microsservicos.exception;

public class UserNotFoundException extends NotFoundException {
  public UserNotFoundException() {
    super("User was not found.");
  }
}
