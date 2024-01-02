package com.microsservicos.productapi.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;

import com.microsservicos.exception.handle.GlobalExceptionHandler;

@ControllerAdvice
public class ProductControllerAdvice extends GlobalExceptionHandler {}
