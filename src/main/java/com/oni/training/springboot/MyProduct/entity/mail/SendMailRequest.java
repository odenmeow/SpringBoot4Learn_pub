package com.oni.training.springboot.MyProduct.entity.mail;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data       //這個本來就有默認無參
@RequiredArgsConstructor //提供final或者NonNull 成員
@AllArgsConstructor// 全部提供
//@NoArgsConstructor// 無參    <有@Data就不需要這個>
@Builder // 鏈結式創建對象
public class SendMailRequest {

    @NotEmpty
    private String subject;
    @NotEmpty
    private String content;
    @NotEmpty
    private String[] receivers;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getReceivers() {
        return receivers;
    }

    public void setReceivers(String[] receivers) {
        this.receivers = receivers;
    }
}
