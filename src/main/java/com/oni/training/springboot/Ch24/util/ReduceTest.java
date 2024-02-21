package com.oni.training.springboot.Ch24.util;

import java.util.List;

public class ReduceTest {
    public static void main(String[] args) {
        List<String> list = List.of("apple", "cat", "meow");
        System.out.println(list.stream().reduce("", String::concat));
        System.out.println(list.stream().reduce("", (x, y) ->x+":"+y));
        System.out.println(list.stream().reduce( (x, y) ->x+":"+y));
        System.out.println(list.stream().reduce( (x, y) ->x+":"+y).get());
        System.out.println(list.stream().reduce( (x, y) ->x+":"+y).orElse("無內容"));
        System.out.println(list);

    }
}
