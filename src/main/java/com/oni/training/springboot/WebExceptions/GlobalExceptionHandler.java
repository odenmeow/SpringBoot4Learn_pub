package com.oni.training.springboot.WebExceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


// 原本只會有異常代碼 和異常類型  但這邊可以回傳消息!
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<String> handleUnprocessableEntityException(UnprocessableEntityException e){
        String errorMessage=e.getMessage();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessage);
    }
}
