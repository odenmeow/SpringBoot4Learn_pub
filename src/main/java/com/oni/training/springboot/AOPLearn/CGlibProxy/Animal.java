package com.oni.training.springboot.AOPLearn.CGlibProxy;

public class Animal {
    public String name ="阿呆";
    public int price =10;
	public String talk(String str) {
        System.out.println("Talking: " + str);
        return str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        System.out.println("getPrice()除了給你錢，順便給錢之前告訴這是誰的錢"+this.hashCode());
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
