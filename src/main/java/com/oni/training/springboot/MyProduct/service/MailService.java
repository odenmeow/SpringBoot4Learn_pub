package com.oni.training.springboot.MyProduct.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.oni.training.springboot.MyProduct.Config.MailConfig;
import com.oni.training.springboot.MyProduct.entity.SendMailRequest;
import jakarta.annotation.PostConstruct;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.*;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import static javax.mail.Message.RecipientType.TO;

@Service
public class MailService {
    // Alt+shift+s +c 可以快速鍵造建構子 !
    // 除此之外 還有 alt+shift+i可以把變數累贅多打的 直接縮短到一行
    // Alt+shift+r 變更名稱 也很方便
    // ctrl+1 或2 和 ctrl+shif+l 也不錯

    private final MailConfig mailConfig;
    private final String TEST_EMAIL ;
    private Address[] RECEIVE_EMAIL;
    private Address[] REPLY_EMAIL;
    private Gmail service;
    private final String authentication_json ;
    // 透過無參建構 生成
    @Autowired
    public MailService(MailConfig mailConfig) throws AddressException {
        this.mailConfig=mailConfig;
        this.TEST_EMAIL=mailConfig.getTest_email();
        List<Address> list= new ArrayList<>();
        for(var s:mailConfig.getReceive_email().split(",")){
            list.add(new InternetAddress(s));
        }

        this.RECEIVE_EMAIL= list.toArray(new Address[list.size()]);

        this.authentication_json=mailConfig.getAuthentication_json();
    }
    @PostConstruct
    private void init() throws Exception {
        REPLY_EMAIL = new InternetAddress[] { new InternetAddress("linc4003931@gmail.com"),
                new InternetAddress("qw28425382694@gmail.com") };
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(HTTP_TRANSPORT, jsonFactory, getCredentials(HTTP_TRANSPORT, jsonFactory))
                .setApplicationName("Test Mailer").build();
        //找不到錯誤?? 因為你沒有tokens2資料夾用來存放credential的地方!!

    }

    /**
     * @deprecated Use {@link #getCredentials(NetHttpTransport,GsonFactory)} instead
     */

    Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {
        // Load client secrets.
        System.out.println(authentication_json);
//        getresourcesTest();

        GoogleClientSecrets clientSecrets =
        // 切記不可以改變檔案名稱 破壞完整性 他驗證失敗會讓你以為改版害的，哪怕裡面資料正確， 結果都會永遠找不到問題!
                GoogleClientSecrets.load(jsonFactory, new InputStreamReader(MailService.class.getClassLoader()
                        .getResourceAsStream(authentication_json)));
//                GoogleClientSecrets.load(jsonFactory, new InputStreamReader(MailService.class
//                        .getResourceAsStream(authentication_json)));
        System.out.println("測試，讀資料有通過");
        // Build flow and trigger user authorization request.
        System.out.println("這邊壞掉嗎"+clientSecrets.getDetails().getClientSecret());
        System.out.println("好像壞掉了");
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
                clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("./tokens").toFile())).setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8889).build();
        // returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void sendMail(SendMailRequest sendMailRequest) throws Exception {
        // 改一下 傳入由String subject, String message變成SendMailRequest
        String subject=sendMailRequest.getSubject();
        String message=sendMailRequest.getContent();
        String[] receivers=sendMailRequest.getReceivers();
        List.of(receivers).forEach(s-> System.out.println(s));
        setReciever(receivers);





        // Encode as MIME message
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(TEST_EMAIL));

//        email.addRecipient(TO, // 使用 ctrl+shift+M就可以 縮短 import 內容
//                new InternetAddress(RECEIVE_EMAIL));
        // Blind carbon copy =秘密副本(複寫紙carbon)
        email.addRecipients(javax.mail.Message.RecipientType.TO,RECEIVE_EMAIL);

        email.setSubject(subject);
        email.setText(message);
        email.setReplyTo(REPLY_EMAIL);
        // Encode and wrap the MIME message into a gmail message
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);
        try {
            // Create the draft message
            msg = service.users().messages().send("me", msg).execute();
            System.out.println("Message id: " + msg.getId());
            System.out.println(msg.toPrettyString());

        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to create draft: " + e.getDetails());
            } else {
                throw e;
            }
        }

    }
    public void setReciever (String[] email) throws AddressException {
        List<Address> list= new ArrayList<>();
        for(var s:email){
            list.add(new InternetAddress(s));
        }
        this.RECEIVE_EMAIL= list.toArray(new Address[list.size()]);

    }
    public void getresourcesTest() throws IOException {

        InputStreamReader R= new InputStreamReader(MailService.class.getClassLoader().getResourceAsStream(authentication_json));
        BufferedReader bf=new BufferedReader(R);
        String line;
        while((line=bf.readLine())!= null){
            System.out.println(line);
        }
    }
    public static void main(String[] args) throws Exception {
//		 Path directory = Paths.get("tokens");
//	        Files.walk(directory)
//	             .sorted(java.util.Comparator.reverseOrder())
//	             .map(Path::toFile)
//	             .forEach(File::delete);
//        InputStreamReader R= new InputStreamReader(MailService.class.getClassLoader().getResourceAsStream("myauth.json"));
//        BufferedReader bf=new BufferedReader(R);
//        String line;
//        while((line=bf.readLine())!= null){
//                System.out.println(line);
//        }
//        MailService m= new MailService(new MailConfig());
//        m.init();
//        m.sendMail("Reset pass Test2!", "check me");
        // 改成這樣就沒辦法測試了 先幹ㄅ

    }
}
