package com.example.together.controller.handler;

import com.example.together.controller.response.ResponseDto;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomeExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseDto<?> handleValidationExceptions(MethodArgumentNotValidException exception) {
    String errorMessage = exception.getBindingResult()
        .getAllErrors()
        .get(0)
        .getDefaultMessage();

    return ResponseDto.fail("BAD_REQUEST", errorMessage);
  }

}
