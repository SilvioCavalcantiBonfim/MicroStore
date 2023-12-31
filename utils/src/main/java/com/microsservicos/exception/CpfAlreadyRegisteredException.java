package com.microsservicos.exception;

public class CpfAlreadyRegisteredException extends RuntimeException {
  public CpfAlreadyRegisteredException() {
    super("The CPF number provided is already registered.");
  }
}
