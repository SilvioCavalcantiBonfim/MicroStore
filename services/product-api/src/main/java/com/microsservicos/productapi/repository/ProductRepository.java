package com.microsservicos.productapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microsservicos.productapi.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

  Product findByProductIdentifier(String productIdentifier);

  @Query(value = "select p from product p join category c on p.category.id = c.id where c.id = :categoryId")
  List<Product> getByCategory(@Param("categoryId") Long categoryId);
}