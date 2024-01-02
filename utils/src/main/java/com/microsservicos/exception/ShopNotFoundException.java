package com.microsservicos.exception;

public class ShopNotFoundException extends NotFoundException {
    public ShopNotFoundException(){
      super("Order was not found.");
    }
}
