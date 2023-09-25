package com.oni.training.springboot.MyProduct.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**創造的意義在於不去更動原始 資料庫交互設定*/
public class ProductRequest {
    private String id;
    //試圖建立對象並且不給值就會出錯
    @NotEmpty(message = "Product name is undefined.Can't be null or empty string ")
    private String name;
    @NotNull //看不懂幹嘛用這個 嗎? 拔掉 跟 留著 兩種
    // 去把get400WhenReplaceProductWithNegativePrice 跑過
    @Min(value = 0, message = "Price should be greater or equal to 0.")
    private Integer price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
