package com.oni.training.springboot.MyProduct.entity;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class CustomBadResponse {
//    private String timestamp=String.valueOf(System.currentTimeMillis());
    private String timestamp=String.valueOf(LocalDateTime.now());
    private String status=String.valueOf(HttpStatus.BAD_REQUEST.value());
    private String error=HttpStatus.BAD_REQUEST.getReasonPhrase();
    private Object message;
    private Object path;
    public CustomBadResponse(Object message,Object path){
        this.message=message;
        this.path=path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getPath() {
        return path;
    }

    public void setPath(Object path) {
        this.path = path;
    }

}
