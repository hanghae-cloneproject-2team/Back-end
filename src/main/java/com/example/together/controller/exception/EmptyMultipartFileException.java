package com.example.together.controller.exception;

public class EmptyMultipartFileException extends RuntimeException {
  public EmptyMultipartFileException() {
    super("multipart file is empty");
  }
}
