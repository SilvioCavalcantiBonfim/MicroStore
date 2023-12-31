package com.microsservicos.exception;

public class IllegalProductException extends RuntimeException{
  public IllegalProductException(){
    super("An invalid product was found in the list. Please check the product details and try again.");
  }
}
