package com.oni.training.springboot.MyProduct.ConverterTest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;


public class ChildOrigin extends ancestor{
    static {
        System.out.println("ChildOrigin靜態啟動");
    }
    @NotEmpty(message = "不可空")
    String name;

    int id;
    int price;
    public ChildOrigin(){

    }
    public ChildOrigin(String name, int id, int price) {
        super(name, price, price);
        this.name = name;
        this.id = id;
        this.price = price;
        System.out.println("ChildOrigin建構啟動");
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

}
