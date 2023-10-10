package com.oni.training.springboot.ConverterTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class man {

    public static void main(String[] args) {
        ApplicationContext ac=new AnnotationConfigApplicationContext(MyConverter.class);

        ((ConfigurableApplicationContext)ac).close();
        System.out.println("========anno搞完換別人搞========");
        ChildOrigin co=new ChildOrigin(null,0,0);

        hi(co);
    }
    // 無論如何，只能夠訪問 父類的屬性跟方法而已 沒辦法透過注入 訪問子類的屬性
    public static void hi(@Autowired @Qualifier("child_2") ancestor a){
        System.out.println("你好"+a.name);
        var c=new Child_2(a.name,a.id,a.price);
        System.out.println(c.name);
    }

}