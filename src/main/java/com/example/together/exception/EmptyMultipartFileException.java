package com.example.together.exception;

public class EmptyMultipartFileException extends RuntimeException {
  public EmptyMultipartFileException() {
    super("multipart file is empty");
  }
}
