package com.microsservicos.productapi.controller;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.microsservicos.dto.ErrorDto;
import com.microsservicos.exception.CategoryNotFoundException;
import com.microsservicos.exception.ProductNotFoundException;

@ControllerAdvice
public class ProductControllerAdvice {

  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({ProductNotFoundException.class, CategoryNotFoundException.class})
  public ErrorDto handleUserNotFound(RuntimeException e) {
    return new ErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage(), new Date());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorDto handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
    StringBuilder message = new StringBuilder();
    message.append("Invalid value for field(s): ");
    String fildsList = e.getBindingResult().getFieldErrors().stream().map(FieldError::getField)
        .collect(Collectors.joining(", "));
    message.append(fildsList);
    message.append(".");
    return new ErrorDto(HttpStatus.BAD_REQUEST.value(), message.toString(), new Date());
  }
}
