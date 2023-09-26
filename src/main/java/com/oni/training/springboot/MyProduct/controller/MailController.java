package com.oni.training.springboot.MyProduct.controller;

import com.oni.training.springboot.MyProduct.entity.SendMailRequest;
import com.oni.training.springboot.MyProduct.service.MailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
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

    @PostMapping
    public ResponseEntity<Void> sendMail(@Valid @RequestBody SendMailRequest request) throws Exception {
        mailService.sendMail(request);
        return ResponseEntity.noContent().build();
    }

}
