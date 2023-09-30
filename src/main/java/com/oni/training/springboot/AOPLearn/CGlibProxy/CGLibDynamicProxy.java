package com.oni.training.springboot.AOPLearn.CGlibProxy;

import org.junit.Before;
import org.springframework.cglib.core.DebuggingClassWriter;

public class CGLibDynamicProxy {
    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/qw284/IdeaProjects/java-basic/jdk-cglib-proxy/target/cglib_proxy_classes");
        CGLibProxy<Animal> cgLibProxy = new CGLibProxy<>();
        Animal a = new Animal();

//        ===============下面 清楚解釋為什麼跟JDK代理不一樣============
//        System.out.println("原始動物的hashcode" + a.hashCode());
        Animal animal = cgLibProxy.getInstance(a);
        animal.talk("壞壞");
//        animal.talk("Hi, 想賣" + animal.name + animal.price + "塊收嗎?");
//        animal.price = 1;
//        System.out.println(animal.name + "只值" + animal.price + "塊");
//        animal.setPrice(8);
//
//        System.out.println(animal.name + "至少要" + animal.price + "塊吧?" + "這應該是代理類的$");
//        System.out.println(animal.name + "至少要" + animal.getPrice() + "塊吧");

//      CGLIB 對象可以直接修改物件屬性 因為是創建子類( 但是實際上調用的所有方法作用於父類 也就是原始傳入的a )
//      雖然 CGLIB (代理)對象 我們稱呼為P_animal吧  Animal animal = cgLibProxy.getInstance(a);
//      P_animal雖然 能繼承可以繼承的部分 ，但是還是會有<private>的方法跟屬性不能接觸 所以實際調用方法是透過
//        class P_animal extends Animal {
//              .....
//              ....
//              @override
//              public void method (){
//                  AOP修改內容 (如果是@Before)
//                  super.method();   交給原始JavaBean調用 我們只是包裹起來。
//                  AOP修改內容 (如果是@After)
//              }
//
//      } 透過這種方式 所以不需要透過反射查詢方法。 另外就是注意不要直接設定值給代理類，最好透過getter、setter進行!
    }
}
