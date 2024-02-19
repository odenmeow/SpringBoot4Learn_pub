package com.oni.training.springboot.Ch24.util;

import io.swagger.v3.oas.models.OpenAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class CommonUtil {
    public static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    private CommonUtil(){

    }
    public static Date toDate(String dateStr){
        try{
            return sdf.parse(dateStr);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
    }
    public static String toSearchText(String s){
        return Optional.ofNullable(s)
                .map(String::trim)  // 移除用戶String 前後多餘的空白 && tab 只留訊息本身(但中間空白可存在)
                .map(String::toLowerCase)
                .orElse("");

        // 簡單講解一下map 如果Optional present ->  映射(map)方法到該obj 身上然後回傳Optional 包裹 (結果)
        // 如果null empty則 回傳empty optional 直到最後觸發orElse
    }
}
