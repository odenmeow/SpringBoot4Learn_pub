package com.oni.training.springboot.MyProduct.Config;

import com.oni.training.springboot.MyProduct.Filter.LogApiFilter;
import com.oni.training.springboot.MyProduct.Filter.LogProcessTimeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    /**
     *     如果沒設定order或者一樣  那麼將會根據方法順序
     *     【】order (0)  (進)先處理 , (出)後處理
     *     {} order (1)  圖像化如下
     *
     *     (請求來嚕)-->>> 【{   }】 (回應送出) ---->>>>
     *
     */
    @Bean
    public FilterRegistrationBean logProcessTimeFilter(){

        FilterRegistrationBean<LogProcessTimeFilter> bean=new FilterRegistrationBean<>();
        bean.setFilter(new LogProcessTimeFilter());
        bean.setOrder(0);// 處理 ( 最先進 , 最後出 )
        bean.addUrlPatterns("/products/*");
        bean.setName("logProcessTimeFilter");
        return bean;
    }
    @Bean
    public FilterRegistrationBean logApiFiler(){
        FilterRegistrationBean<LogApiFilter> bean=new FilterRegistrationBean<>();
        bean.setFilter(new LogApiFilter());
        bean.setOrder(1);
        bean.addUrlPatterns("/products/*");
        bean.setName("logApiFilter");
        return bean;
    }
}
