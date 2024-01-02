package com.microsservicos.exception.handle;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.microsservicos.exception.CpfAlreadyRegisteredException;
import com.microsservicos.exception.IllegalProductException;
import com.microsservicos.exception.InvalidCpfLengthException;
import com.microsservicos.exception.NotFoundException;

public abstract class GlobalExceptionHandler {

  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public final ErrorDto handleNotFound(NotFoundException e) {
    return new ErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(InvalidCpfLengthException.class)
  public final ErrorDto handleInvalidCpfLength(InvalidCpfLengthException e) {
    return new ErrorDto(HttpStatus.UNPROCESSABLE_ENTITY.value(), e.getMessage(), LocalDateTime.now());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(CpfAlreadyRegisteredException.class)
  public final ErrorDto handleCpfAlreadyRegistered(CpfAlreadyRegisteredException e) {
    return new ErrorDto(HttpStatus.CONFLICT.value(), e.getMessage(), LocalDateTime.now());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public final ErrorDto handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
    StringBuilder message = new StringBuilder();
    message.append("The parameter '");
    String parameterName = e.getParameterName();
    message.append(parameterName);
    message.append("' is missing.");
    return new ErrorDto(HttpStatus.BAD_REQUEST.value(), message.toString(), LocalDateTime.now());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ErrorDto handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
    StringBuilder message = createMenssage(e);
    return new ErrorDto(HttpStatus.BAD_REQUEST.value(), message.toString(), LocalDateTime.now());
  }

  private final StringBuilder createMenssage(MethodArgumentNotValidException e) {
    StringBuilder message = new StringBuilder();
    String fildsList = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
        .collect(Collectors.joining(", "));
    message.append(fildsList);
    message.setCharAt(0, Character.toUpperCase(message.charAt(0)));
    message.append('.');
    return message;
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MissingRequestHeaderException.class)
  public final ErrorDto handleMissingRequestHeader(MissingRequestHeaderException e) {
    StringBuilder message = new StringBuilder();
    message.append("The parameter '");
    String parameterName = e.getHeaderName();
    message.append(parameterName);
    message.append("' is missing.");
    return new ErrorDto(HttpStatus.BAD_REQUEST.value(), message.toString(), LocalDateTime.now());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public final ErrorDto handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
    StringBuilder message = new StringBuilder();
    message.append("The parameter '");
    String parameterName = e.getPropertyName();
    message.append(parameterName);
    message.append("' is missing.");
    return new ErrorDto(HttpStatus.BAD_REQUEST.value(), message.toString(), LocalDateTime.now());
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ IllegalAccessException.class, IllegalProductException.class })
  public final ErrorDto handleIllegalAccess(RuntimeException e) {
    return new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage(), LocalDateTime.now());
  }

  // @ResponseBody
  // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  // @ExceptionHandler(RuntimeException.class)
  // public final ErrorDto handleRuntime(RuntimeException e) {
  //   return new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error has occurred.",
  //       LocalDateTime.now());
  // }
}
