package com.oni.training.springboot.AOPLearn.Compose;


import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/AOP",produces = MediaType.APPLICATION_JSON_VALUE)
public class AOPController {

    @Autowired
    CGlib_instance cGlib_instance;
    @Autowired
    JDK_father jdkInstance;

    List<Long> cglib=new ArrayList<>();
    List<Long> jdk=new ArrayList<>();

    @GetMapping("/{proxy}")
    public void jdkTest(@PathVariable(name = "proxy") String name){
        if("jdk".equalsIgnoreCase(name)){
            long start=System.nanoTime();
            jdkInstance.sayhi();
            long end=System.nanoTime();
            jdk.add(end-start);
//            jdkInstance.sayhi0();

            System.out.println(jdkInstance+":"+jdkInstance.hashCode());
            System.out.println(jdkInstance.getClass()+":"+jdkInstance.getClass().hashCode());

        }
        if("cglib".equalsIgnoreCase(name)){
            long start=System.nanoTime();
            cGlib_instance.sayhi();
            long end=System.nanoTime();
            cGlib_instance.sayhi();

            cglib.add(end-start);
            System.out.println(cGlib_instance+":"+cGlib_instance.hashCode()+":"+cGlib_instance.hashCode());
            System.out.println(cGlib_instance.getClass()+":"+cGlib_instance.getClass().hashCode());
        }
        if("avg".equalsIgnoreCase(name)){
            long jdk_sum=jdk.stream()
                            .skip(1)
                            .mapToLong(Long::longValue)
                            .sum();
            long cglib_sum=cglib.stream()
                            .skip(1)
                            .mapToLong(Long::longValue)
                            .sum();
            System.out.printf("CGlib=%d   ,   jdk=%d",cglib_sum,jdk_sum);
        }
    }

}
