package com.oni.training.springboot.MyProduct.controller;

import com.oni.training.springboot.MyProduct.entity.mail.SendMailRequest;
import com.oni.training.springboot.MyProduct.service.MailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/mail",produces = MediaType.APPLICATION_JSON_VALUE)
public class MailController {

    private MailService mailService;
    @Autowired

    public MailController(MailService mailService){
        this.mailService=mailService;
    }

//     調用方法的時候 實際上是對 單一實例 MailService mail 這個傢伙提出想要調用，然後傳入，剩下就由api進行內部的thread 安全處理。
//    建議MailService為單例( 因為早就被設計成threadsafe) 如果要做成多例 那還要去改變setPort(自動分配port) 這反而麻煩也不太必要。
    @PostMapping
    public ResponseEntity<Void> sendMail(@Valid @RequestBody SendMailRequest request) throws Exception {
        mailService.sendMail(request);
        return ResponseEntity.noContent().build();
    }

}
