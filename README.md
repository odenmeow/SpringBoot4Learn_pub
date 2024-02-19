# 學習來源

https://chikuwa-tech-study.blogspot.com/search/label/Spring%20Boot 



## 使用前注意:

### docker pull 我的 image

- 透過 docker hub 取得我已經架設的 ubuntu + mongodb Image，直接使用、避免中毒危險 + 節省部屬時間，以Postman搭配下面swagger api 快速了解、直接使用Project。

### 使用 swagger可以browser 查看 api 消息

> [Swagger UI]([Swagger UI](http://localhost:8080/swagger-ui/index.html#/)) 
> 
> 二選一都一樣
> 
> http://localhost:8080/swagger-ui/index.html#/  





## 專案架構 請忽略

- AOPLearn

- Ch24

- ConverterTest

- JacksonExample

- LombokExample

- myStudy

- WebCrawler

## 只需看下面三包

### MyProduct

- aop (  自製切面、annotation 練習  )

- auth ( 認證相關 )

- config ( 配置相關 )
  
  - FilterConfig  
  
  - MailConfig 
  
  - ServiceConfig  ( Product、Mail )
  
  - SwaggerConfig ( api閱讀手冊基本設置 )

- controller ( route 相關動作 )

- converter ( DTO )

- DBConfiguration
  
  - InitialClearDB ( 初始化刪除舊資料 )

- entity
  
  - app_user
  
  - mail
  
  - product
  
  - CustomBadResponse ( 搭配WebExeptions使用 )

- Filter 
  
  - LogApiFilter
  
  - LogProcessTimeFilter 
  
  - 上述兩者是配置類所引用的

- repository
  
  - AppUserRepository ( 使用者Repo )
  
  - ProductRepository ( 產品 )

- Service
  
  - MailService ( 信件、使用 Gmail 寄信)
  
  - ProductService ( 信件自訂義annotation、產品CRUD )

- 

### WebExceptions

- GlobalExceptionHandler ( 主要處理req例外者 )

### test 測試

- java
  
  - com.oni.training.springboot
    
    - integration
    
    - unit 
    
    






















