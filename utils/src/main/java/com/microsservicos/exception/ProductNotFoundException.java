package com.microsservicos.exception;

public class ProductNotFoundException extends NotFoundException {
  public ProductNotFoundException() {
    super("Product was not found.");
  }
}
