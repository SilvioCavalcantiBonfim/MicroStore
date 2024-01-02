package com.microsservicos.shoppingapi.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;

import com.microsservicos.exception.handle.GlobalExceptionHandler;

@ControllerAdvice
public class ShopControllerAdvice extends GlobalExceptionHandler {}
