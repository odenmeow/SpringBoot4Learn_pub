package com.oni.training.springboot.MyProduct.entity.app_user;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum UserAuthority {
    // 完全不知道他幹嘛寫這個 她文章好像沒用到????  我不知道

    ADMIN,NORMAL;

    @JsonCreator
    public UserAuthority fromString(String key){

        return Arrays.stream(values())
                .filter(value->value.name().equalsIgnoreCase(key))
                .findFirst()
                .orElse(null);
    }
}
