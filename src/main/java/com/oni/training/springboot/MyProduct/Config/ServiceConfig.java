package com.oni.training.springboot.MyProduct.Config;


import com.oni.training.springboot.MyProduct.repository.ProductRepository;
import com.oni.training.springboot.MyProduct.service.MailService;
import com.oni.training.springboot.MyProduct.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.mail.internet.AddressException;

@Configuration
public class ServiceConfig {

    private MailConfig mailConfig;
    @Autowired
    public ServiceConfig(MailConfig mailConfig){
        this.mailConfig=mailConfig;
    }
    @Bean
    @Autowired
    @DependsOn("InitialClearDB")  // 在 < 調用該方法創建 >之前 會先創建另一個bean
    /**  【重點一】: ScopedProxyMode.TARGET_CLASS >>>>>   ProductService 每次呼叫時就建立一個全新的元件 ( CGLIB )
     *                                              1.  >>  前提是 prototype
     *                                              2.  >>  任何方法都會建立全新物件
         【重點二】: if MailService 也 == Prototype 則會 隨著 ProductService 重新產生
                                                    1.  >>  不會刪除已經產生的
                                                    2.  >>  自行呼叫  prototypeInstance.clear("把佔據的資源刪除，否則無限累積")

         【註】  : singleton 一開始容器就生成   &&   prototype 也是 (if without further setting )。
                 >>不管 DI幾次 都是引用相同對象      >> 根據 DI 使用次數 為不同對象

                                                                            >> DI = 當作 @Autowired

        【proxyMode  用于解决依赖注入时的代理问题】
     */                                             // >> 這邊跟一般CGLIB沒關聯 而是baseOn原本的又多出來的
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,proxyMode = ScopedProxyMode.TARGET_CLASS)
//                                       >>這下面repository 是 singleton 所以不會受影響
    public ProductService productService(ProductRepository repository,MailService mailService){
        System.out.println("Product Service is created.");
        return new ProductService(repository, mailService);
    }

    @Bean
    @DependsOn("mailconfig")  //採用依賴注入 最大程度迴避  錯誤
//    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE )  //使用這種常量方式可以減少出錯!
    @RequestScope
    public MailService gmail_user() throws AddressException {
        // 我的跟原作者有點不同 因為我用Gmail提供的而不是通用的低安全性的JAVAMail 。
        // 我的最多就是多個不同帳密的user 然後只是我這邊傳入配置類this 如果真的要多個那就自己小改囉!
        MailService mailService=new MailService(mailConfig);
        System.out.println("已建立mailService物件");
        return mailService;
    }
}
