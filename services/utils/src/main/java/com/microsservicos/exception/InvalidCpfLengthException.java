package com.microsservicos.exception;

public class InvalidCpfLengthException extends RuntimeException {
  public InvalidCpfLengthException() {
    super("The CPF number provided is invalid. It must contain exactly 11 digits.");
  }
}
