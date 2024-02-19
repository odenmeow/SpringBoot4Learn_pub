# 學習來源

https://chikuwa-tech-study.blogspot.com/search/label/Spring%20Boot 

## 使用前注意:

### docker pull 我的 image

- 透過 docker hub 取得我已經架設的 ubuntu + mongodb Image，直接使用、避免中毒危險 + 節省部屬時間，以Postman搭配下面swagger api 快速了解、直接使用Project。
  
  ```batch
  docker pull onini/public-repo:mongo
  ```
  
  ```batch
  docker pull onini/public-repo:tutu
  ```
  
  ```batch
  docker images 
  ```
  
  - 可以查看是否有存在這兩個image。
    
    - 假設有 
    
    - `tutu-copy-img-public` ，tag為 tutu 。
    
    - `mongodb-image-public` ，tag為mongo 。

#### docker 使用方式

- 已經取得`images`了，接著使用來建立 `containers` 。
  
  ```batch
  docker run -it -p 8083:8080 --name tutu-copy tutu-copy-img-public
  ```
  
  ```batch
  docker run -it -p 27017:27017 --name  mongodb mongodb-image-public
  ```
  
  - 得到兩個容器 分別是 `tutu-copy` 、`mongodb` 。
  
  - 跑成功後去GUI看，可以直接停止。

- 替兩個容器建立互相連結的網路環境

##### 容器之間的網路

- 透過`GUI` `Docker Desktop`可以 直接查看容器，點進去後，去看一下`Inspect`  最底下，看看是否存在 `mongodb_network` 這個東西，如果兩個都有，就不需要做下面的。

- 如果都沒有則
  
  ```batch
  docker network ls
  ```
  
  ```batch
  docker network connect mongodb_network tutu-copy
  ```
  
  ```batch
  docker network connect mongodb_network mongodb
  ```
  
  - 請去 `GUI`  `Docker Desktop` > `mongodb` > `inspect` 查看最下面 `mongodb_network` ，下面的 IPAddress ，記住 例如下面 172.18.0.3。
    
    ```json
    "Networks": {
                "mongodb_network": {
                    "IPAMConfig": null,
                    "Links": null,
                    "Aliases": [
                        "mongodb",
                        "mongodb",
                        "0e9e7b6de2c5"
                    ],
                    "NetworkID": "eaed95799c3eba28b41d35596c9eff331ad825f16483a506a549bd52eee79f6a",
                    "EndpointID": "bab90c7df428ea021676c903e4db9bb3db031539137d54b09f557efc0d0ff913",
                    "Gateway": "172.18.0.1",
                    "IPAddress": "172.18.0.3",
                    "IPPrefixLen": 16,
                    "IPv6Gateway": "",
                    "GlobalIPv6Address": "",
                    "GlobalIPv6PrefixLen": 0,
                    "MacAddress": "02:42:ac:12:00:03",
                    "DriverOpts": null
                },
    ```

- 建立好網路之後，去 `tutu-copy` 內部 設定 `test` 跟 `main` 的 `application.properties` 。

##### tutu的 db設定 (main+test)

- 完成網路之間的連接後，要去設定 `MongoDB` 的 `uri` 。
  
  - `main` 的 application.properties
  
  - `test` 的 application.properties
    
    因為我有安裝 `nano` 請直接使用指令更改

###### 注意，我是使用 SpringBoot4Learn_pub 唷。

- 請不要在這邊隨便使用pull，我已經改過內容了，如果失敗就自己從`image`再做一次，
  
  如果想要pull，則需要自己處理merge，下面注意事項有說，哪些不要被merge替換。

- > `main` 
  
  ```batch
  cd /my-github-projects/SpringBoot4Learn_pub
  ```
  
  ```batch
  cd /my-github-projects/SpringBoot4Learn_pub/src/main/resources
  ```
  
  ```batch
  nano application.properties
  ```
  
  ```java
  spring.data.mongodb.uri=mongodb://aaa:ccc@172.18.0.3:27017
  spring.data.mongodb.database=demo
  ```
  
  - `IP` 改為剛剛 要求你記下來的 mongodb 內 `inspect`攜帶的 mongodb_network，所提供的那個IPAddress。
  
  - 改好之後，`ctrl+s`  保存，  `ctrl+x` 離開。

- > `test` 
  
  ```batch
  cd /my-github-projects/SpringBoot4Learn_pub/src/test/resources
  ```
  
  ```batch
  nano application.properties
  ```
  
  - 同上。

#### Docker 兩個容器都啟動後，tutu-copy 啟動內部Springboot專案

- 容器之間ip、mongodb uri設定好之後，啟動springboot專案。
  
  ```batch
  mvn spring-boot:run 
  ```

⭐ 應該是成功了 ⭐

##### ⚠️可能發生的問題⚠️

- `GOOGLE Credential` 的問題，如果過期之類，需要我登入授權 `app` 才可以。
  
  - 要刪除 token資料夾下 StoredCredential 然後重新運作 (使用到email 的部分)，才會觸發要求我驗證，透過某uri。

- Email 給我 qw28425382694@gmail.com ，我會再更新 Git Repository。

- pull 的時候 請先 切換到 `SpringBoot4Leran_public`
  
  ```batch
  git stash save yourstashName
  ```
  
  - 使用自己想要給的該 stash 名稱 
  
  - 雖然之後會使用 git stash pop 直接跳出，但做點識別比較好。
  
  ```batch
  git pull 
  ```
  
  ```batch
  git stash pop
  ```
  
  - 如果有衝突，請保持自己原本設定的 pom.xml、test、main的application.properties。

### 完成後，請透過下面從容器外面的網頁連接 容器內部的服務 !

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
