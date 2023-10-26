package com.oni.training.springboot.MyProduct.entity;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
//https://zhuanlan.zhihu.com/p/266377497 對@Builder注解的說明是不建議與構造函數注解和@EqualsAndHashCode一起使用的 !!!!!
//https://medium.com/%E7%A8%8B%E5%BC%8F%E7%8C%BF%E5%90%83%E9%A6%99%E8%95%89/java-%EF%B8%8F-%E5%AE%A2%E8%A3%BD%E5%8C%96-lombok-builder-%E7%9A%84%E6%96%B9%E6%B3%95-%E4%B8%80-%E5%82%B3%E5%85%A5-null-%E6%8E%A1%E7%94%A8%E9%A0%90%E8%A8%AD%E5%80%BC-d9cf76b5b2b
public class CustomBadResponse {
//      文章中提到的下面兩句，指的是類似 ()->{}、匿名函數 的時候 傳入需要是effective final 或者final ，因為創建後非立刻使用
//         { 雖然這個做法有個小缺點，就是不能使用 final ，但這個缺點需要看情況：
//              只有當物件內部要拿欄位值做操作時，不能使用 final 才會是問題。}
//      不然就需要用[] 然後 陣列傳入 避免編譯報錯
//    上面四句餵GPT 它會告訴你原因

//    private String timestamp=String.valueOf(System.currentTimeMillis());
//    @Builder.Default 不要用，需要看情況用，如果不小心Builder有傳入.timestamp(null) 會被覆蓋設為 null 而非default !
    @Getter(AccessLevel.NONE)
    private String timestamp=String.valueOf(LocalDateTime.now());
    private String status=String.valueOf(HttpStatus.BAD_REQUEST.value());
    private String error=HttpStatus.BAD_REQUEST.getReasonPhrase();
    private Object message;
    private Object path;
    public CustomBadResponse(Object message,Object path){
        this.message=message;
        this.path=path;
    }

//    public static class PersonBuilder{
//        private String timestamp="0";
//        public PersonBuilder timestamp(String timestamp){
//            this.timestamp=timestamp==null?"0":timestamp;
//            return this;
//        }
//    } 設定時避免null傳入 (想要預設值還是要至少呼叫) builder.timestamp()  先block 我不喜歡

    // 自己設定getter 就可以避開builder不設定會產出null的問題
    /** 如果timestamp 是 null 使用預設值 ，不是null代表被設定過 所以可以直接使用 */
    public String getTimestamp() {
        return timestamp==null?String.valueOf(LocalDateTime.now()):timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getPath() {
        return path;
    }

    public void setPath(Object path) {
        this.path = path;
    }

}
