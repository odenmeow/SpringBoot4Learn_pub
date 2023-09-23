package com.oni.training.springboot.WebCrawler;

import java.io.File;

public class Filemaker {
    public static void main(String[] args) {
        // [js]
        // ./images     目前檔案所在的內部
        // /images      專案開啟的那個root內部
        // ../          上一層資料的下面
        // images       目前檔案所在的內部

        // [spring]
        //  "/Images"   會建立在C:/Images 這邊
        //  "Images"    會建立在springboot專案下方
        //  "./Images"    還是會建立在springboot專案下方
        //  "/src/main/java/com/oni/training/springboot/WebCrawler/Images" 不會建立 因為沒這個磁碟名稱
        //  "src/main/java/com/oni/training/springboot/WebCrawler/Images" 建立!
        Filemaker fmk=new Filemaker();
        fmk.create_withClassloader();
        fmk.easesit();
    }
    public void easesit(){
        System.out.println("========我是最簡單==========");
        String currentClassLoaderLocation = getClass().getClassLoader().getResource("").getPath();
        System.out.println(currentClassLoaderLocation);
    }
    public void create_withClassloader(){
        // 1. 獲取當前執行程式碼所在的目錄（工作目錄）
        String currentWorkingDirectory = System.getProperty("user.dir");

        // 2. 使用Java的類別載入器獲取執行程式碼的位置

        ClassLoader classLoader = this.getClass().getClassLoader();//this.getClass().getClassLoader()
        System.out.println("Iam: "+classLoader.getName());// 會是app 因為上面那段 回傳是SpringBoot 導致
        String classname=getClass().getName(); //你也可以用this.getClass().getName();
        var StringRebuilder=new Filemaker(){
          public String trimToOnlyName_class(String classname){
              int lastindex=classname.lastIndexOf(".");
              StringBuilder sb=new StringBuilder();
              sb.append(classname.substring(lastindex+1,classname.length()));
              sb.append(".class");
              return sb.toString();
          }
        };classname= StringRebuilder.trimToOnlyName_class(classname);
        // classname will be [this fileName].class



        classLoader=Thread.currentThread().getContextClassLoader(); //.getName()依舊還是app呵呵
        //資料必須要在resources才能取得到!
        //String classLocation = getClass().getClassLoader().getResource("Filemaker.class").getPath();

        var StringRebuilder2=new Filemaker(){
            public String expand_fullPathName(String classname){
                int lastindex=classname.lastIndexOf(".");
                StringBuilder sb=new StringBuilder();
                sb.append("src/main/java/");

                sb.append(classname.replace(".","/"));
                return sb.toString();
            }
        };
        String classLocation=StringRebuilder2.expand_fullPathName(getClass().getName());

        System.out.println(classLocation);
        // 移除.class文件名以獲取目錄路徑
        int lastSlashIndex = classLocation.lastIndexOf("/");
        String classDirectory = classLocation.substring(0, lastSlashIndex);

        // 3. 創建目錄
        String newDirectoryName = "new_directory"; // 要創建的目錄名稱
        File newDirectory = new File(classDirectory + "/" + newDirectoryName);

        if (newDirectory.exists()) {
            System.out.println("目錄已存在。");
        } else {
            boolean created = newDirectory.mkdir();
            if (created) {
                System.out.println("目錄已成功創建。");
            } else {
                System.out.println("無法創建目錄。");
            }
        }
    }
    public static void create_withDirectory(){
        System.out.println(System.getProperty("user.dir"));
        File f=new File("src/main/java/com/oni/training/springboot/WebCrawler/Images");
        if (!f.exists()) {
            f.mkdir();
            System.out.println("成功建立");

        }else {
            System.out.println("失敗!");
        }
    }
}
