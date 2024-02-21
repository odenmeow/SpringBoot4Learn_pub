package com.oni.training.springboot.Ch24.controller;

import com.oni.training.springboot.Ch24.exception.OperateAbsentItemsException;
import com.oni.training.springboot.Ch24.model.BatchDeleteRequest;
import com.oni.training.springboot.Ch24.model.BusinessExceptionType;
import com.oni.training.springboot.Ch24.model.ExceptionResponse;
import com.oni.training.springboot.Ch24.model.ProductTF;
import com.oni.training.springboot.Ch24.util.CommonUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/ch24/products",produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductControllerCH24 {

    private final Map<String, ProductTF> productDB=new LinkedHashMap<>();

    @PostConstruct
    private void initData(){
        List<ProductTF> products= List.of(
                ProductTF.of("B1", "Android (Java)", "2022-01-15"),
                ProductTF.of("B2", "Android (Kotlin)", "2022-05-15"),
                ProductTF.of("B3", "Data Structure (Java)", "2022-09-15"),
                ProductTF.of("B4", "Finance Management", "2022-07-15"),
                ProductTF.of("B5", "Human Resource Management", "2022-03-15")
        );
        products.forEach(p->productDB.put(p.getId(),p));
    }

    @GetMapping
    public ResponseEntity<List<ProductTF>> getProducts(
//      @RequestParam(required = false, name = "productName") String name) { 如果希望param填入可以不同於參數的話，否則默認相同
        @RequestParam(required = false) Date createdFrom,
        @RequestParam(required = false) Date createdTo,
        @RequestParam(required = false) String name){

        var stream=productDB.values().stream();
        //請注意stream操作過一次後就 無法再度當作新的stream使用囉!
        if(createdFrom!=null){
//            因為 有使用 @InitBinder了，所以會自動前面 先轉換
//            var from= CommonUtil.toDate(createdFrom);
//            stream=stream.filter(p->p.getCreatedTime().after(from));
            stream=stream.filter(p->p.getCreatedTime().after(createdFrom));
        }
        if(createdTo!=null){
//            var to= CommonUtil.toDate(createdTo);
//            stream=stream.filter(p->p.getCreatedTime().before(to));
            stream=stream.filter(p->p.getCreatedTime().before(createdTo));
            // 回傳是必須的因為他是intermediate操作 會關閉舊的streamA 產生新的streamB
            // 不回傳 新的就不會被re assign到 stream變數，用B覆蓋舊的A
        }
        if(name!=null){
            var n=CommonUtil.toSearchText(name);
            stream=stream.filter(p->p.getName().toLowerCase().contains(n));
        }
        // 如果都沒填的話
        var products=stream.collect(Collectors.toList());
        return ResponseEntity.ok(products);


    }
    @DeleteMapping
    public ResponseEntity<Void> deleteProducts(@RequestBody BatchDeleteRequest request){
        List<String> itemIds=request.getIds();
        var absentIds=itemIds.stream()
                .filter(Predicate.not(productDB::containsKey))
                .collect(Collectors.toList());
        if(!absentIds.isEmpty()){
            // 不合格 is Empty 代表合格 -> 繼續往下
            // 否則 ( 加上 ! ) 丟出訊息 說誰不合格
            throw new OperateAbsentItemsException(absentIds);
        }
        itemIds.forEach(productDB::remove);
        return ResponseEntity.noContent().build();
    }
//    下面這團  只對 該 controller 有效。 因採用 controllerAdvice 故，隱藏。
//    @ExceptionHandler(OperateAbsentItemsException.class)
//    public ResponseEntity<ExceptionResponse> handleOperateAbsentItem(OperateAbsentItemsException e) {
//        Map<String, Object> info = Map.of("itemIds", e.getItemIds());
//        var res = new ExceptionResponse();
//        res.setType(BusinessExceptionType.OPERATE_ABSENT_ITEM);
//        res.setInfo(info);
//
//        return ResponseEntity.unprocessableEntity().body(res); //因為這邊已經幫我拋出狀態碼了
//    }
}
