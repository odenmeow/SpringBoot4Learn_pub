package com.oni.training.springboot.WebExceptions;


import com.oni.training.springboot.MyProduct.entity.CustomBadResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;


// 原本只會有異常代碼 和異常類型  但這邊可以回傳消息!
@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(UnprocessableEntityException.class)
//    public ResponseEntity<String> handleUnprocessableEntityException(UnprocessableEntityException e){
//        String errorMessage=e.getMessage();
//        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessage);
     /**                     上面舊版只會回傳錯誤消息跟狀態碼 但是body顯示 不是json也不夠詳細!                   */
//    }
    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<CustomBadResponse> handleUnprocessableEntityException(UnprocessableEntityException e){
        String errorMessage=e.getMessage();
        Map<String,String> errorMap=new HashMap<>();
        errorMap.put("errorMessage",errorMessage);
        var unprocessableJson=CustomBadResponse.builder()
                .message(errorMap)
                .path(ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath())
                .error(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .status(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()).build();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(unprocessableJson);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomBadResponse> handleNotFoundEntityException(NotFoundException e){
        String errorMessage=e.getMessage();
        Map<String,String> errorMap=new HashMap<>();
        errorMap.put("errorMessage",errorMessage);

        var notfoundJson=CustomBadResponse.builder()
                .message(errorMap)
                .path(ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath())
                .error(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notfoundJson);
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<CustomBadResponse> handleConflictException(ConflictException e){
        String errorMessage=e.getMessage();
        Map<String,String> errorMap=new HashMap<>();
        errorMap.put("errorMessage",errorMessage);
/**             下面這一個用法會回傳 null  不應該這樣使用 而是第二個才正確     上面都要改              **/
//        var bodyJson=new CustomBadResponse(errorMap,ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath())
//                .builder()
//                .error(String.valueOf(HttpStatus.CONFLICT.value()))
//                .status(HttpStatus.CONFLICT.getReasonPhrase())
//                .build();
/**               正確使用方法                                          **/
        var bodyJson=CustomBadResponse.builder()
                .message(errorMap)
                .path(ServletUriComponentsBuilder.fromCurrentRequestUri().build().getPath())
                .error(String.valueOf(HttpStatus.CONFLICT.value()))
                .status(HttpStatus.CONFLICT.getReasonPhrase())

                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(bodyJson);

    }
}
