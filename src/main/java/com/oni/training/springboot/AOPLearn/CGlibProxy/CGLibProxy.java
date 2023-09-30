package com.oni.training.springboot.AOPLearn.CGlibProxy;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class CGLibProxy<T> implements MethodInterceptor {
    private T target;

    public T getInstance(T target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        long start = System.nanoTime();
        System.out.println("引用之前我先說句話!");
        System.out.println(target + ": ===CGLib proxy===");
        Object result = methodProxy.invoke(this.target, args);
//        System.out.println("我這邊是AOP代理子類\t\t\t"+this.hashCode());
        System.out.println(target + ": ===CGLib proxy===");
        long end = System.nanoTime();
        System.out.println("Executing time: " + (end - start) + " ns");

        return result;
    }
}
