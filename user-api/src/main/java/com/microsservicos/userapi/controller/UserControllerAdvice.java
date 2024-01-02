package com.microsservicos.userapi.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;

import com.microsservicos.exception.handle.GlobalExceptionHandler;

@ControllerAdvice
public class UserControllerAdvice extends GlobalExceptionHandler {}
