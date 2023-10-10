package com.oni.training.springboot.MyProduct.entity.app_user;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppUser {


    private String id;
    // 設定索引避免 email 重複 導致創帳號 錯誤!  spring.data.mongodb.auto-index-creation: true 要啟動
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String emailAddress;
    private String password;
    private String name;
    // 不使用下面兩個注釋會被認作是關聯表  需另外創建表格保存role值
    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING) //讓他以字串儲存在資料庫
    private List<Role> role;




//    private List<UserAuthority> authorities;
    // 可以設定多個角色!

}
