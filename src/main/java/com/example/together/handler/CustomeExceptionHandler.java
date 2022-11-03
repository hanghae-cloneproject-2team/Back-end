package com.example.together.handler;

import com.example.together.exception.RestApiException;
import com.example.together.controller.response.ResponseDto;
import com.example.together.error.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomeExceptionHandler {

  //ErrorCode
//  @ExceptionHandler(MethodArgumentNotValidException.class)
//  public ResponseDto<?> handleValidationExceptions(MethodArgumentNotValidException exception) {
//    String errorMessage = exception.getBindingResult()
//        .getAllErrors()
//        .get(0)
//        .getDefaultMessage();
//
//    return ResponseDto.fail("BAD_REQUEST", errorMessage);
//  }

  //바인드 에러
  @ExceptionHandler({BindException.class})
  protected ResponseEntity<Object> handleServerException(BindException ex) {
    RestApiException error = new RestApiException(ErrorCode.BIND_Fails.name(), ErrorCode.BIND_Fails.getMessage());
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
  }
  //회원가입 정보 확인
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleApiRequestException(MethodArgumentNotValidException ex) {
    List<RestApiException> errors=new ArrayList<>();

    for( FieldError field : ex.getBindingResult().getFieldErrors()){
      errors.add(new RestApiException(field.getField(),field.getDefaultMessage()));
    }

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errors);
  }

}
