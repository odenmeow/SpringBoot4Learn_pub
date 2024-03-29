package com.oni.training.springboot.AOPLearn.Compose;


import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@RestController
@RequestMapping(path = "/AOP",produces = MediaType.APPLICATION_JSON_VALUE)
public class AOPController {

    @Autowired
    CGlib_instance cGlib_instance;  // 在Ch15-2的測試中 為CGLIB沒錯
    @Autowired
    JDK_father jdkInstance;         // 不使用介面會報錯 因為有介面就會先被以JDK 所以注入要介面

    List<Long> cglib=new ArrayList<>();
    List<Long> jdk=new ArrayList<>();
    List<Object> cgKeeper=new ArrayList<>();
    List<Object> jdKeeper=new ArrayList<>();

    @GetMapping("/{proxy}")
    public void jdkTest(@PathVariable(name = "proxy") String name){
        if("jdk".equalsIgnoreCase(name)){
            long start=System.nanoTime();
            jdkInstance.sayhi();
            long end=System.nanoTime();
            jdk.add(end-start);
            jdKeeper.add(jdkInstance);
//            System.out.println(jdkInstance.getClass());
        }
        if("cglib".equalsIgnoreCase(name)){
            long start=System.nanoTime();
            cGlib_instance.sayhi();
            long end=System.nanoTime();
            cglib.add(end-start);
            cgKeeper.add(cGlib_instance);
        }
        if("avg".equalsIgnoreCase(name)){
            OptionalDouble jdk_sum=jdk.stream()
                            .skip(1)
                            .mapToLong(Long::longValue)
                            .average();
            OptionalDouble cglib_sum=cglib.stream()
                            .skip(1)
                            .mapToLong(Long::longValue)
                            .average();
            System.out.printf("CGlib=%f   ,   jdk=%f",cglib_sum.orElseThrow(),jdk_sum.orElseThrow());
            System.out.printf("\ncgkeeper %d, jdkeeper %d",cgKeeper.size(),jdKeeper.size());


        }
    }

}
