package com.oni.training.springboot.MyProduct.controller;

import com.oni.training.springboot.MyProduct.entity.mail.SendMailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@Tag(name = "MailControllerApi" ,description = "Email control, just send mails.")

public interface MailControllerApi {
    //     調用方法的時候 實際上是對 單一實例 MailService mail 這個傢伙提出想要調用，然後傳入，剩下就由api進行內部的thread 安全處理。
    //    建議MailService為單例( 因為早就被設計成threadsafe) 如果要做成多例 那還要去改變setPort(自動分配port) 這反而麻煩也不太必要。
    @PostMapping
    @Operation(summary = "send Email", description = "send email by this controller,provide your JWT<br> 麻煩使用自己的emails ",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Successfully send, no response body, only headers.",
                            content = @Content(mediaType = "application/json"),

                            headers = {
                                    @Header(name = "Cache-Control", description = "no-cache, no-store, max-age=0, must-revalidate"),
                                    @Header(name = "Connection", description = "keep-alive"),
                                    @Header(name = "Date", description = "Tue, 20 Feb 2024 08:44:08 GMT"),
                                    @Header(name = "Expires", description = "0"),
                                    @Header(name = "Keep-Alive", description = "timeout=60"),
                                    @Header(name = "Pragma", description = "no-cache"),
                                    @Header(name = "X-Content-Type-Options", description = "nosniff"),
                                    @Header(name = "X-Frame-Options", description = "DENY"),
                                    @Header(name = "X-XSS-Protection", description = "0")
                            }
                    ),  //不寫content會給預設的回傳body 示例
                    @ApiResponse(responseCode = "403",
                            description = "Only authenticate user can do this ,or Maybe not permit url, or having the wrong password",
                            content = @Content),            //寫了但沒給schema 會只剩文字
                    @ApiResponse(responseCode = "404",
                            description = "Not found such" ,
                            content = @Content),
                    @ApiResponse(responseCode = "400",
                            description = "Against format regulation" ,
                            content = @Content)
            }
    )
    ResponseEntity<Void> sendMail(@Valid @RequestBody SendMailRequest request) throws Exception;
}
