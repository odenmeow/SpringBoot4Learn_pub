package com.oni.training.springboot.myStudy;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

class MyClass {
    public void myMethod() {
        System.out.println("This is my method.");
    }
}
public class referenceComparison {

    public static void main(String[] args) throws NoSuchMethodException {
        MyClass obj1 = new MyClass();
        MyClass obj2 = new MyClass();

        // 將方法引用賦值給變數
        Runnable methodRef1 = obj1::myMethod;
        Runnable methodRef2 = obj1::myMethod;

        // 檢查方法引用是否相等




        System.out.println(methodRef1 == methodRef2);   //false
        // false，因為它們引用了不同的方法



        // 檢查方法引用是否相等
        System.out.println(methodRef1.equals(methodRef1));  // 依舊false


        Runnable m=new Runnable() {
            @Override
            public void run() {
                int x=10;
            }
        };
        int y=10;
        Runnable m2= ()->{
            String.valueOf(y);
        };
//         y=11; // effectively final才可以唷~
        Function<Integer,Integer> fn=(Integer x)->{
//            x=10**2;
//            應該使用
            return (int) Math.pow(x,2);
        };
        System.out.println(fn.apply(5));


//         透過     反射取得方法倒是可以

        Method method1 = MyClass.class.getMethod("myMethod");
        Method method2 = MyClass.class.getMethod("myMethod");

        System.out.println(method1.equals(method2)); // 將會是 true










    }

}
