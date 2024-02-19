package com.oni.training.springboot.Ch24.controller;

import com.oni.training.springboot.Ch24.model.ProductTF;
import com.oni.training.springboot.Ch24.util.CommonUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductController {

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
        @RequestParam(required = false) String createdFrom,
        @RequestParam(required = false) String createdTo,
        @RequestParam(required = false) String name){

        var stream=productDB.values().stream();
        //請注意stream操作過一次後就 無法再度當作新的stream使用囉!
        if(createdFrom!=null){
            var from= CommonUtil.toDate(createdFrom);
            stream=stream.filter(p->p.getCreatedTime().after(from));
        }
        if(createdTo!=null){
            var to= CommonUtil.toDate(createdTo);
            stream=stream.filter(p->p.getCreatedTime().before(to));
            // 回傳是必須的因為他是intermediate操作 會關閉舊的streamA 產生新的streamB
            // 不回傳 新的就不會被re assign到 stream變數，用B覆蓋舊的A
        }

        if(name!=null){
            var n=CommonUtil.toSearchText(name);
            stream=stream.filter(p->p.getName().toLowerCase().contains(n));
        }
        var products=stream.collect(Collectors.toList());
        return ResponseEntity.ok(products);


    }
}
