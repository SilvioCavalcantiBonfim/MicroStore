package com.microsservicos.shoppingapi.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Item {

  private String productIdentifier;
  private Float price;
  private Integer amount;

  public Integer getAmount() {
    return amount;
  }
  public void setAmount(Integer amount) {
    this.amount = amount;
  }
  public String getProductIdentifier() {
    return productIdentifier;
  }
  public void setProductIdentifier(String productIdentifier) {
    this.productIdentifier = productIdentifier;
  }
  public Float getPrice() {
    return price;
  }
  public void setPrice(Float price) {
    this.price = price;
  }
}
