package com.microsservicos.productapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microsservicos.dto.ProductDto;
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
  @ResponseStatus(HttpStatus.OK)
  public List<ProductDto> retrieveAllProducts(){
    return productService.retrieveAllProducts();
  }

  @GetMapping("category/{id}")
  @ResponseStatus(HttpStatus.OK)
  public List<ProductDto> retrieveProductsByCategoryId(@PathVariable(name = "id") Long id){
    return productService.retrieveProductsByCategory(id);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductDto retrieveProductByIdentifier(@PathVariable(name = "id") String id){
    return productService.retrieveProductByIdentifier(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductDto addNewProduct(@Valid @RequestBody ProductDto productDto){
    return productService.addProduct(productDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeProductById(@PathVariable(name = "id") String id){
    productService.removeProductByIdentifier(id);
  }

}
