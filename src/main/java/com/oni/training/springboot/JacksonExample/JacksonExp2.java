package com.oni.training.springboot.JacksonExample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JacksonExp2 {

    public static void main(String[] args) throws JsonProcessingException {

        ObjectMapper mapper=new ObjectMapper();

        Cats cats = new Cats();
        List<Cat> catList = new ArrayList<>();

        Cat cat1 = new Cat();
        cat1.setName("Whiskers");
        cat1.setSex(true);
        cat1.setAge(3);
        cat1.setColor("Grey");

        Cat cat2 = new Cat();
        cat2.setName("Mittens");
        cat2.setSex(false);
        cat2.setAge(5);
        cat2.setColor("White");

        catList.add(cat1);
        catList.add(cat2);

        cats.setCats(catList);

        String StrCats=mapper.writeValueAsString(cats);
        System.out.println(StrCats);


//        List<Cat> StrReader=mapper.readValue(StrCats,Cat.class);
        Cats StrReader=mapper.readValue(StrCats,Cats.class);
        StrReader.getCats().stream().forEach(c->{
            System.out.println( c.getAge());
            System.out.println( c.getColor());
            System.out.println(  c.getName());



        });
    }
    @Getter
    @Setter
    private static class Cats{
        private List<Cat> cats;

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
