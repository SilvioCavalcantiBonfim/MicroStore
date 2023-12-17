package com.microsservicos.userapi.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.microsservicos.dto.ErrorDto;
import com.microsservicos.exception.CpfAlreadyRegisteredException;
import com.microsservicos.exception.InvalidCpfLengthException;
import com.microsservicos.exception.UserNotFoundException;

@ControllerAdvice
public class UserControllerAdvice {

  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(UserNotFoundException.class)
  public ErrorDto handleUserNotFound(UserNotFoundException e){
    return new ErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage(), new Date());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(InvalidCpfLengthException.class)
  public ErrorDto handleInvalidCpfLength(InvalidCpfLengthException e){
    return new ErrorDto(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage(), new Date());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(CpfAlreadyRegisteredException.class)
  public ErrorDto handleCpfAlreadyRegistered(CpfAlreadyRegisteredException e){
    return new ErrorDto(HttpStatus.CONFLICT.value(), e.getMessage(), new Date());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ErrorDto handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
    StringBuilder message = new StringBuilder();
    message.append("The parameter '");
    String parameterName = e.getParameterName();
    message.append(parameterName);
    message.append("' is missing.");
    return new ErrorDto(HttpStatus.BAD_REQUEST.value(), message.toString(), new Date());
  }
}
