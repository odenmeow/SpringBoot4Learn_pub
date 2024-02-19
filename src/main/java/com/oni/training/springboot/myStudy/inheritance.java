package com.oni.training.springboot.myStudy;

public class inheritance {

    public static void main(String[] args) {

        System.out.println(B.a());
//        int a=5;
//        String b="你好";
//        System.out.println(a+b);
//        String c=a+b;
//        System.out.println(c);
//        float d=5+5.5f;
//        System.out.println(d);
//        System.out.println((int)d);
//        String e=5+"";

    }
    
    protected static class  A{
        public static String a(){
            return "你好";
        }
    }
    protected static class B extends A{
//        public static String a(){
//            return "不好";
//        }
    }
}
