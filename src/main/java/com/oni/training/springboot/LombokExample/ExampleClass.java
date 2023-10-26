package com.oni.training.springboot.LombokExample;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

//https://kucw.github.io/blog/2020/3/java-lombok/   可以參考這個網址
//  都不啟用@  預設就會給我一個   自動有無傳參數建構式
//  有任何一個有傳參數被設定 那就不會自動預設 無傳參數建構式

//@Data                   // 居然會給我nonNullprice的參數建構式 如果nonNullprice頭上 @NonNull拿掉，變成無傳參數
//@NoArgsConstructor
//@RequiredArgsConstructor    //  搭配著youInput 和nonNullprice ; 有賦值不生成 建構式 !
//@AllArgsConstructor // static不會被包含。有包含 transient 、private 、final(沒預設才生成，其他非final被預設也會生成)
public class ExampleClass implements Serializable {
    public static String publicStaticField = "Public Static Field";
    private int privateField=5;
    @NonNull
    public final String finalField = "Final Field";
//    public final String youInput;
    @NonNull
    private int nonNullprice;
    private static String Iamstatic;
    private transient String transientField = "Transient Field";

}