package com.example.together.controller.exception;

public class FileConvertException extends RuntimeException {

  public FileConvertException() {
    super("fail convert multipartfile to file");
  }
}
