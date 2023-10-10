package com.oni.training.springboot.MyProduct.auth.auth_user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
    private String email;
    private String password;


}
