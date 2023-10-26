package com.oni.training.springboot.JacksonExample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

public class JacksonExp {

    public static void main(String[] args) throws JsonProcessingException {

        ObjectMapper j=new ObjectMapper();
        Person xin=new Person();
        xin.setAge(5);
        xin.setName("小xin");
        String xinString=j.writeValueAsString(xin);
        System.out.println(xinString);
        Person read =j.readValue(xinString,Person.class);
        System.out.println(read.age);
        System.out.println(read.name);

        Cat cat=new Cat();
        cat.setAge(5);
        cat.setSex(true);
        cat.setName("小呼");
        cat.setColor("RED");
        String catString=j.writeValueAsString(cat);
        //沒給getter或setter都不可 沒辦法讀取值寫入 就不生成該字段 (不會報錯、所以你不會發現)
        System.out.println(catString);
        Cat catRead=j.readValue(catString,Cat.class);
        System.out.println(catRead.sex);
        System.out.println(catRead.age);
        System.out.println(catRead.name);
        System.out.println(catRead.color);
//        不提供某屬性Getter 最多就那個屬性沒有被輸出而已
//        {"name":"小呼","sex":true,"age":5}
//        原本還有color : null


    }

    @Getter
    @Setter
    private static class Person{
        private String name;
        private int age;

    }
    @Getter
    @Setter
    private static class Cat{
        private String name;
        private boolean sex;
        private int age;
        private String color;



    }
}
