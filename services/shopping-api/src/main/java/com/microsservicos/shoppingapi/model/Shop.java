package com.microsservicos.shoppingapi.model;

import java.util.Date;
import java.util.List;

import com.microsservicos.dto.ShopDto;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity(name = "shop")
public class Shop {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userIdentifier;

  private Double total;

  private Date date;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "item", joinColumns = @JoinColumn(name = "shop_id"))
  private List<Item> items;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserIdentifier() {
    return userIdentifier;
  }

  public void setUserIdentifier(String userIdentifier) {
    this.userIdentifier = userIdentifier;
  }

  public Double getTotal() {
    return total;
  }

  public void setTotal(Double total) {
    this.total = total;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  public static Shop convert(ShopDto shopDto){
    Shop shop = new Shop();
    shop.setUserIdentifier(shopDto.userIdentifier());
    shop.setTotal(shopDto.total());
    shop.setDate(shopDto.date());
    shop.setItems(shopDto.items().stream().map(Item::convert).toList());
    return shop;
  }

}
