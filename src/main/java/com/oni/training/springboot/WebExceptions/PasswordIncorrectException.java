package com.oni.training.springboot.WebExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class PasswordIncorrectException extends RuntimeException  {
    public PasswordIncorrectException(String msg) {
        super(msg);
    }
}
