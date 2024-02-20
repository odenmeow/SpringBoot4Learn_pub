package com.oni.training.springboot.MyProduct.entity.mail;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data       //這個本來就有默認無參
@RequiredArgsConstructor //提供final或者NonNull 成員
@AllArgsConstructor// 全部提供
//@NoArgsConstructor// 無參    <有@Data就不需要這個>
@Builder // 鏈結式創建對象
public class SendMailRequest {
    @Schema(description = "Subject主旨", example = "打招呼")

    @NotEmpty
    private String subject;
    @Schema(description = "content內容", example = "hihi")

    @NotEmpty
    private String content;
    @NotEmpty
    @Schema(description = "receivers,array of string(email)", example= """
            ["qw28425382694@gmail.com","linc4003931@gmail.com"]
                                     
            """ )

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
