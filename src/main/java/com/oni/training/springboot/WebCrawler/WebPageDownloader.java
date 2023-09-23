package com.oni.training.springboot.WebCrawler;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebPageDownloader {
    public static void main(String[] args) {
        try {
            // 指定要抓取的網址
            URL url = new URL("https://zh.wikipedia.org/zh-tw/%E7%B6%B2%E8%B7%AF%E7%88%AC%E8%9F%B2");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            // 讀取網頁內容
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();

            // 將網頁內容保存到本地檔案
            FileWriter writer = new FileWriter("webpage.html");
            writer.write(content.toString());
            writer.close();

            // 使用本地瀏覽器打開本地檔案
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                // 如果是 Windows 作業系統
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler webpage.html");
            } else if (os.contains("mac")) {
                // 如果是 macOS 作業系統
                Runtime.getRuntime().exec("open webpage.html");
            } else {
                // 其他作業系統，可能需要自行調整
                System.out.println("無法自動打開瀏覽器");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}