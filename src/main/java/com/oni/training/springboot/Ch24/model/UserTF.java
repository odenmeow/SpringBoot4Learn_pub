package com.oni.training.springboot.Ch24.model;

//TF代表24  twenty-four

import lombok.Getter;

@Getter
public class UserTF {
    private String id;
    private String name;
    private String email;

    public static UserTF of(String id, String name, String email) {
        var user = new UserTF();
        user.id = id;
        user.name = name;
        user.email = email;
        return user;
    }

}
