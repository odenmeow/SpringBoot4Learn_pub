package com.oni.training.springboot.WebExceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)  //404
public class NotFoundException extends RuntimeException{
    public NotFoundException(String msg){
        super(msg);
        //把有傳參數建構式的內容 super給父親的建構式並調用之 -> 父親_func(msg)。
    }
}
