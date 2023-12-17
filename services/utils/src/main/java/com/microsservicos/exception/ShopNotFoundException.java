package com.microsservicos.exception;

public class ShopNotFoundException extends RuntimeException {
    public ShopNotFoundException(){
      super("Order was not found.");
    }
}
