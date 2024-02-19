package com.oni.training.springboot.MyProduct.aop;

public enum EntityType {
    // 雖然寫的是fun 但是一定要幫他寫上參數給他 蠻奇妙...
    PRODUCT("product"),APP_USER("user");
    private String presentName;
    // 另外，預設constructor = private ， 無法對外開放為public !
    EntityType(String presentName){
        this.presentName=presentName;
    }

    @Override
    public String toString() {
        return presentName;
    }
}
