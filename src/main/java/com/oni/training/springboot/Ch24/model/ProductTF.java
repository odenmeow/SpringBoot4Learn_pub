package com.oni.training.springboot.Ch24.model;

import com.oni.training.springboot.Ch24.util.CommonUtil;
import lombok.Getter;

import java.util.Date;

//TF代表24  twenty-four
@Getter
public class ProductTF {

    private String id;
    private String name;
    private Date createdTime;

    public static ProductTF of(String id,String name,String createdTime){
        var product=new ProductTF();
        product.id=id;
        product.name=name;
        product.createdTime= CommonUtil.toDate(createdTime);
        return product;
    }

}
