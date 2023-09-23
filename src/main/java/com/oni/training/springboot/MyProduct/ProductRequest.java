package com.oni.training.springboot.MyProduct;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ProductRequest {
    private String id;
    @NotEmpty(message = "Product name is undefined.")
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
