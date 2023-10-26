package com.oni.training.springboot.MyProduct.aop;

public class Main {
    public static void main(String[] args) {
        ActionType a=ActionType.CREATE;
        System.out.println("CREATE".contentEquals(a.toString()));

        EntityType e=EntityType.APP_USER;
        System.out.println(e);
    }
}
