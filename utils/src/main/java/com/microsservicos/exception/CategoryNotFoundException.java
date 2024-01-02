package com.microsservicos.exception;

public class CategoryNotFoundException extends NotFoundException {
  public CategoryNotFoundException() {
    super("Category was not found.");
  }
}
