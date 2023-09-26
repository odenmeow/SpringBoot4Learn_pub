package com.oni.training.springboot.MyProduct.Config;

import com.oni.training.springboot.MyProduct.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.mail.internet.AddressException;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port:25}") // 使用「:」符號可以加上預設值
    private int port;

    @Value("${mail.auth.enabled}")
    private boolean authEnabled;

    @Value("${mail.starttls.enabled}")
    private boolean starttlsEnabled;

    @Value("${mail.protocol}")
    private String protocol;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    /** Added for Gmail.*/
    @Value("${mail.test_email}")
    String test_email;
    @Value("${mail.receive_email}")
    String receive_email;

    public String getAuthentication_json() {
        return authentication_json;
    }

    @Value("${mail.authentication_json}")
    String authentication_json;

    public String getTest_email() {
        return test_email;
    }

    public String getReceive_email() {
        return receive_email;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isAuthEnabled() {
        return authEnabled;
    }

    public boolean isStarttlsEnabled() {
        return starttlsEnabled;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    @Bean
    public MailService gmail_user() throws AddressException {
        // 我的跟原作者有點不同 因為我用Gmail提供的而不是通用的低安全性的JAVAMail 。
        // 我的最多就是多個不同帳密的user 然後只是我這邊傳入配置類this 如果真的要多個那就自己小改囉!
        MailService mailService=new MailService(this);
        return mailService;
    }






}
