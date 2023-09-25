package com.oni.training.springboot.WebExceptions;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

//@ControllerAdvice(basePackages = "com.oni.training.springboot.controller")
//@Order(Ordered.HIGHEST_PRECEDENCE) //自訂跟spring 都想代理那個MethodArgumentNotValidException 所以會錯誤。
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)

    public Map<String,String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err->{
            String fieldName=((FieldError)err).getField();
            String errorMessage=err.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        return errors;
    }
}
