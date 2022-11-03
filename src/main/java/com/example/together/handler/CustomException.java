//package com.example.together.handler;
//
//
////import com.example.together.exception.ErrorCode;
//import com.example.together.controller.response.ResponseDto;
//import com.example.together.error.ErrorCode;
//import lombok.Getter;
//
//@Getter
//public class CustomException extends RuntimeException {
//    private ErrorCode error;
//
//    public CustomException(ErrorCode e) {
//        super(e.getMessage());
//        this.error = e;
//    }
//    public static ResponseDto<?> toResponse(CustomException e){
//        return ResponseDto.fail("error",e.error.getMessage());
//    }
//}
