package com.oni.training.springboot.MyProduct.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)  //runtime>class>source
public @interface SendEmail {
    EntityType entity();
    ActionType action();
    int idParamIndex() default  -1;// -1代表回傳值中有id  -2代表沒有id(改用回傳值email)  0代表去跟方法找id
}
