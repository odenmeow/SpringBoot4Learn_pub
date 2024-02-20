package com.oni.training.springboot.MyProduct.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

public class ProductResponse  {
    @Schema(description = "id that you provided.",example = "65d440fcafce1d56df946b30")
    private String id;
    @Schema(description = "product name.",example = "Android Development (Java)")

    private String name;
    @Schema(description = "product price.",example = "380")

    private int price;
    @Schema(description = "if login and insert a product, it will contains.",example = "null")

    private String creatorId;

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
