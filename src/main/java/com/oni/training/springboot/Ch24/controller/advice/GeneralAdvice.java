package com.oni.training.springboot.Ch24.controller.advice;

import com.oni.training.springboot.Ch24.controller.ProductControllerCH24;
import com.oni.training.springboot.Ch24.controller.UserControllerCH24;
import com.oni.training.springboot.Ch24.exception.OperateAbsentItemsException;
import com.oni.training.springboot.Ch24.model.BusinessExceptionType;
import com.oni.training.springboot.Ch24.model.ExceptionResponse;
import com.oni.training.springboot.Ch24.util.CommonUtil;
import com.oni.training.springboot.Ch24.util.ToSearchTextEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.Map;

// 指定作用在哪些  class 身上
@RestControllerAdvice(assignableTypes = {ProductControllerCH24.class, UserControllerCH24.class})
public class GeneralAdvice {
    // 下面第一個 為 spring 自帶的轉換器
    private final CustomDateEditor customDateEditor=new CustomDateEditor(CommonUtil.sdf,true);
    // 第二個為半自訂的，透過繼承
    private final ToSearchTextEditor toSearchTextEditor=new ToSearchTextEditor();
    @ExceptionHandler(OperateAbsentItemsException.class)
    public ResponseEntity<ExceptionResponse> handleOperateAbsentItem(OperateAbsentItemsException e){
        Map<String,Object> info=Map.of("itemIds",e.getItemIds());
        var res=new ExceptionResponse();
        res.setType(BusinessExceptionType.OPERATE_ABSENT_ITEM);
        res.setInfo(info);
        return ResponseEntity.unprocessableEntity().body(res);
    }
    @InitBinder(value = {"createdFrom","createdTo"})
    public void bindDate(WebDataBinder binder){
        binder.registerCustomEditor(Date.class,customDateEditor);

    }
    @InitBinder({"name","email"})
    public void bindSearchText(WebDataBinder binder){
        binder.registerCustomEditor(String.class,toSearchTextEditor);
    }
}
