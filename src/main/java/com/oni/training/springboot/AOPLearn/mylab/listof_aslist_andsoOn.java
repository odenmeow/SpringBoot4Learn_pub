package com.oni.training.springboot.AOPLearn.mylab;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class listof_aslist_andsoOn {
    public static void main(String[] args) {

        int[] arr ={1,2,3,4,5,6};
        Arrays.stream(arr).forEach(e-> System.out.println(e));
        List lst=Arrays.stream(arr).boxed().collect(Collectors.toList());
        lst.add(777);
        lst.forEach(e-> System.out.println(e));

/**             結果都印出記憶體位置  因為不是這樣玩的   */
        System.out.println("方法二 as List");
        List lst2=Arrays.asList(arr);
//        lst2.add(888);        不可以變更 因為asList  跟  List.of 一樣都產出不能變的
        lst2.forEach(e-> System.out.println(e));



        System.out.println("方法三  List.of");
        List lst3=List.of(arr);
//        lst3.add(999);
        lst3.forEach(System.out::println);




        System.out.println("方法四  stream.toList");
        List lst4=Arrays.stream(arr).boxed().toList();
        lst4.add(999);
        lst4.forEach(System.out::println);


    }
}
