package com.oni.training.springboot.ConverterTest;

import jakarta.validation.constraints.NotNull;


public class Child_2 extends ancestor{
    static {
        System.out.println("Child_2靜態啟動");
    }
    @NotNull
    public String name;


    int id;
    int price;
    public Child_2(){
    }
    public Child_2(String name, int id, int price) {
        super(name, id, price);
        System.out.println("Child_2建構啟動");
    }
    public void talk(){
        System.out.println("i can talk with u");
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