package com.oni.training.springboot.AOPLearn.JDKProxy;

import java.lang.reflect.Proxy;

public class JDKDynamicProxy {

	public static void main(String[] args) {
		ClassLoader classLoader =JDKDynamicProxy.class.getClassLoader();

		Class<?>[] interfaces=Bird.class.getInterfaces();
		Bird bird=new Bird();
		Flyable flyable=(Flyable)Proxy
				.newProxyInstance(classLoader, interfaces, new FlyableInvocation(bird));

		flyable.fly("Go to pee.");
//		System.out.println();
//		其實你也印不出屬性 因為這是代理類 只有幫你實現方法 (並且調用時會替換(應該說方法內部有invoke去實踐真實對象) )
//		建議都要使用GETTER SETTER 雖然你無法直接操作 但是避免有問題還是小心點。
	}

}
