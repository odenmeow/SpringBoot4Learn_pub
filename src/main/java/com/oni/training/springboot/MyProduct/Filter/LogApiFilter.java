package com.oni.training.springboot.MyProduct.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;

public class LogApiFilter  extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        ContentCachingRequestWrapper requestWrapper=new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper=new ContentCachingResponseWrapper(response);


        /**    ---------------------    CH 16 Filter 重點就在這了  -----------------
         *                         ( 第五小節 跳過 )Configuration 比較好用
         *
         *           body內容需要用 wrapper 避免讀取後就沒了，先領包裹 再做doFilter
         *
         *          東西被人拿完做完  controller都要回傳了   你才想打開 request.body
         *           byte[] 主體早就被controller和其他人 拿完  你來不及複製了
         *
         *          【注意】:
         *                  ContentCachingRequestWrapper 一 定 要
         *
         *                  經  過        Controller 做了getInputStream     才會有內容 。
         *
         *                  而且 Controller的 RequestBody 必定會使用 getInputStream 但是這玩意只能被用一次
         *
         *                  除了那一次  其他人想要複製只能透過    getContentAsByteArray
         *
         *                  如果沒人使用 getInputStream  也不能觸動 getContentAsByteArray 只會為空 !
         *
         *                  為 了 提 高 效 率  ???? 我也不懂ㄏㄏ
         *
         *                  new String(StreamUtils.copyToByteArray(requestWrapper.getInputStream()));
         *                  先做這個可以在 doFilter之前讀取 requestWrapper.getContentAsByteArray 但是
         *                  Controller就會讀不到 getInputStream了
         *                  Stream特性就是只能讀取一次
         * */


//       除了請求體內容以外，HttpServletRequest 中還包含了請求的各種元數據，例如請求方法、URI、請求頭、請求參數等。
//       這些"元數據"  可以被多次讀取，因為它們是基於 HTTP 請求的元資訊，不涉及請求體內容的流式讀取

        filterChain.doFilter(requestWrapper,responseWrapper);
        logBody(requestWrapper,responseWrapper);
        logAPI(requestWrapper,responseWrapper);

        responseWrapper.copyBodyToResponse();


/**          LogAPI 是擷取 META 資料 < 非stream >所以沒關係 所以照舊拿取 is okay.     */



    }
    private void logBody(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        String requestBody = getContent(request.getContentAsByteArray());
        System.out.println("Request:"+showInvisible(requestBody));
        // 不知道哪裡來的bug 讓字都看不見 但確實length() =69 所以發現被隱藏
        String responseBody = getContent(response.getContentAsByteArray());
        System.out.println("Response: " + responseBody);
    }
    public String showInvisible(String requestBody){
        return requestBody.replaceAll("[\\p{C} ]", "");
    }
    public void showInvisibleOneByOne(String requestBody){
        for (int i = 0; i < requestBody.length(); i++) {
            char c = requestBody.charAt(i);
            System.out.println("Character at position " + i + ": " + (int) c + " (" + c + ")");
        }
    }
    public String getContent(byte[] content){
        String body=new String(content);
        return body.replaceAll("[\t\n]","");
    }
    public void logAPI(HttpServletRequest request,HttpServletResponse response){


        String params=request.getQueryString();
        String uri=request.getRequestURI();
        String httpMethod=request.getMethod();
        int httpStatus=response.getStatus();
        if(params!=null){
            uri+="?"+params;
        }
        System.out.println(
                String.join(" ",
                        String.valueOf(httpStatus),
                        httpMethod,
                        uri)
        );
    }
}
