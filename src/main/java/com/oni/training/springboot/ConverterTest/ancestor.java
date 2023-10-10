package com.oni.training.springboot.ConverterTest;


import org.springframework.stereotype.Component;


public abstract class ancestor {
    public String name;
    public int id;
    public int price;
    static {
        System.out.println("ancestor靜態啟動");
    }
    public ancestor(){

    }
    public ancestor(String name2, int id2, int price2) {
        System.out.println("ancestor建構啟動");
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
