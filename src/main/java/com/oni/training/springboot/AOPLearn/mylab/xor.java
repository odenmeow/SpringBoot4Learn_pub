package com.oni.training.springboot.AOPLearn.mylab;

import java.util.Arrays;

public class xor {
    public static void main(String[] args) {

        int[] arr=new int[9];
        Arrays.fill(arr,7);


//        arr[(int)(Math.random()*1000+1)]=(int)(Math.random()*100+1);;
        arr[(int)(Math.random()*9+1)]=5;
        stupid(arr);

//      偶數的重複的話  結果就是正確  如果奇數重複 則 要再跟重複的做一次XOR 才是
        /**
         *      原始題目是:    [2,4,5,6,5,4,2] 中  有一個數字單獨 其他成對   使用互斥或 可以找出獨立的傢伙!
         *
         *
         *    0 XOR 7 = 7
         *               7  XOR 7 = 0
         *                  然後順序無所謂重點是重複幾次
         *    7^7^7=7    7^7=0   偶數次 相同互斥或得到 0
         *
         *    重複的7 偶數的話  =0
         *
         * **/
        xor(arr);
        System.out.println("2 ^ 7="+ (2^7));
//        Arrays. stream(arr).boxed().collect(Collectors.toList()).forEach(e-> System.out.println(e));
        System.out.println(Arrays.stream(arr).boxed().count() );
    }
    public static void xor(int[] arr){
        var a=0;
        // 重複項為偶數次的  速解
        for(int i=0;i<arr.length;i++){
//            System.out.println("before:"+a);
            a ^= arr[i];
//            System.out.println("after:"+a);
        }
        System.out.println("你好"+a);
    }
    public static void stupid(int[] arr){
        int x=0;
        Integer multiple=null;
        Integer difference=null;
        if(arr[0]==arr[1]){
            multiple=arr[0];
        }else{
            if(arr[0]!=arr[2]){
                difference=arr[0];
            }
            if(arr[1]!=arr[2]){
                difference=arr[1];
            }
        }

        if( difference !=null){
            System.out.println("找到了不一樣的是"+difference);}
        else {
            System.out.println("發現重複者，進入迴圈找唯一不同者");
            for (int i = 0; i < arr.length; i++) {
                if(arr[i]!=multiple){
                    System.out.println("迴圈中找到了不同者"+arr[i]);
                }
            }
        }
    }
}
