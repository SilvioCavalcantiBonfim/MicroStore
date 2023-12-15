package com.microsservicos.productapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microsservicos.productapi.dto.ProductDto;
import com.microsservicos.productapi.exception.ProductNotFoundException;
import com.microsservicos.productapi.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService){
    this.productService = productService;
  }

  @GetMapping
  public List<ProductDto> getAll(){
    return productService.getAll();
  }

  @GetMapping("category/{id}")
  public List<ProductDto> getByCategory(@PathVariable Long id){
    return productService.getProductByCategoryId(id);
  }

  @GetMapping("/{id}")
  public ProductDto findById(@PathVariable String id){
    return productService.findByProductIdentifier(id);
  }

  @PostMapping
  public ProductDto createProduct(@Valid @RequestBody ProductDto productDto){
    return productService.save(productDto);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws ProductNotFoundException{
    productService.delete(id);
  }

}
