package com.oni.training.springboot.MyProduct.auth.auth_user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthRequest {

    @NotBlank
//    @JsonProperty("emailAddress") 如果要改外界登入使用的Json field的話 加上這個 外面打emailAddress就會對應裡面email:"aa@ggmail"
    @Schema(description = "Should be provide with none blank.",example = "qw28425382694@gmail.com")
    private String email;
//    @Schema(description = "The password of the user.",maximum = "20",minimum = "6")
    @Schema(description = "The password of the user.",example = "aa1234")
//    由於下面有 他會自己解析 我就不提供重複的訊息了
    @Size(min = 6,max = 20,message = "password should between 6 and 20")
    private String password;


}
