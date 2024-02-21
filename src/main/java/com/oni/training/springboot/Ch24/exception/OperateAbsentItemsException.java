package com.oni.training.springboot.Ch24.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

//@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
// 因為ProductControllerCH24已經寫 @ExceptionHandler 所以這邊不需要用了 ，雖然好像用了也沒事???
public class OperateAbsentItemsException extends RuntimeException{
    private final List<String> itemIds;
    public OperateAbsentItemsException(List<String> itemIds){
        super("Following ids are non-existent"+itemIds);
//        super("Following ids are non-existent"+itemIds.stream().reduce((x,y)->x+":"+y).orElse("無法呈現"));
        this.itemIds=itemIds;
    }
    public List<String> getItemIds() {
        return itemIds;
    }
}
