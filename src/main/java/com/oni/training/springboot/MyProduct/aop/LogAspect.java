package com.oni.training.springboot.MyProduct.aop;


import jakarta.persistence.criteria.Join;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
@Aspect
public class LogAspect {

    @Pointcut("execution(* com.oni.training.springboot.MyProduct.service..*(..))")
    public void pointcut(){

    }
    private String getMethodName(JoinPoint joinPoint){
        MethodSignature signature=(MethodSignature) joinPoint.getSignature();
        return signature.getName();
    }
    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        System.out.println("=========================before advice starts=========================");
        // 方法   (Controller調用的)
        System.out.println(getMethodName(joinPoint));
        // 參數 傳入controller的參數
        Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
        System.out.println("=========================before advice ends=========================");
    }

    // After 即使出現例外也會執行這邊的內容!
    @After("pointcut()")
    public void after (JoinPoint joinPoint){
        System.out.println("=========================after advice starts=========================");
        System.out.println(getMethodName(joinPoint));
        Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
        System.out.println("=========================after advice ends=========================");
    }
    // Before > method > AfterReturning > After
    //AfterReturning　如果發生例外則不執行
    @AfterReturning(pointcut = "pointcut()",returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result){

        System.out.println("======after returning advice starts======");
        if(result!=null){
            System.out.println(result);
            //com.oni.training.springboot.MyProduct.entity.product.ProductResponse@6ad5c69 之類
        }

        System.out.println("======after returning advice ends======");
    }

    @Around("pointcut()")
    public Object around (ProceedingJoinPoint joinPoint ) throws Throwable {
        System.out.println("========================================around advice starts========================================");
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long spentTime = System.currentTimeMillis() - startTime;
        System.out.println("Time spent: " + spentTime);
        System.out.println("========================================around advice ends========================================");
        return result; //這之後還會輸出 :
                        //          創建好之後有id了為:6527c70b05f73f0d8a3bcb6d
                        //          Request:{"name":"ComputerNoob","price":1,"hihi":"這個並不會被加進去但可以加入成功上面"}
                        //          Response: {"id":"6527c70b05f73f0d8a3bcb6d","name":"Computer Noob","price":1}
                        //          201 POST /products
                        //          RequestProcessTime :260 ms
                        //      這是因為 這不是service的範疇 而是controller調用service之後 手動印出來的 下面Request那些則是SpringBoot預設的!
    }
    // 如果拋出例外就執行以下
    @AfterThrowing(pointcut = "pointcut()",throwing = "throwable")
    public void afterThrowing(JoinPoint joinPoint,Throwable throwable){
        System.out.println("=====after throwing advice starts=====");
        System.out.println(getMethodName(joinPoint));
        Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);
        System.out.println(throwable.getMessage());
        System.out.println("=====after throwing advice ends=====");
    }

}


