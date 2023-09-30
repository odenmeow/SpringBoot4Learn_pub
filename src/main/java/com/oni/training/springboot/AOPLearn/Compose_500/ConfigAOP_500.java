package com.oni.training.springboot.AOPLearn.Compose_500;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = false)

public class ConfigAOP_500 {
//    https://blog.csdn.net/qq_32077121/article/details/107805007
//    bean的作用域
//      首先，我们需要了解下Spring定义了多种作用域:
//      1.单例（Singleton）：在整个应用中，只创建bean的一个实例。
//      2.原型（Prototype）：每次注入或者通过spring应用上下文获取的时候，都会创建一个新的bean实例。
//      3.会话（Session）：在web应用中，为每个会话创建一个bean实例。
//      4.请求（Request）：在Web应用中，为每个请求创建一个bean实例。
    @Pointcut("execution (* com.oni.training.springboot.AOPLearn.Compose_500.JDK_instance_500.*(..))")
    public void pointcutJDK() {
    }
    @Pointcut("execution (* com.oni.training.springboot.AOPLearn.Compose_500.CGlib_instance_500.*(..))")
    public void pointcutCGlib() {
    }
//    @Around("pointcutJDK() || pointcutCGlib()" )
//    public void AroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        String methodName = joinPoint.getSignature().getName();
//        List<Object> args = Arrays.asList(joinPoint.getArgs());
////		System.out.println("V2呼叫方法= "+methodName+"內容"+args);
//        System.out.println("-------------Before Advice送出的訊息：方法 " + methodName + " 開始執行，傳入的參數為 " + args
//            + "時間:"+new Date().getTime()
//        );
//        joinPoint.proceed();
//        System.out.println("-------------Before Advice送出的訊息：方法 " + methodName + " 開始執行，傳入的參數為 " + args
//                + "時間:"+new Date().getTime()
//        );
//    }
    @Before("pointcutJDK() || pointcutCGlib()" )
    public void beforeMethod(JoinPoint joinPoint) {

//        String methodName = joinPoint.getSignature().getName();
//        List<Object> args = Arrays.asList(joinPoint.getArgs());
//		System.out.println("V2呼叫方法= "+methodName+"內容"+args);
        System.out.println("--Before Advice");
    }
    @After("pointcutJDK() || pointcutCGlib()" )
    public void AfterMethod(JoinPoint joinPoint) {

        System.out.println("--After Advice");
    }

    private String getMethodName(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getName();
    }
}
