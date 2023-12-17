package com.microsservicos.exception;

public class CategoryNotFoundException extends RuntimeException {
  public CategoryNotFoundException() {
    super("Category was not found.");
  }
}
