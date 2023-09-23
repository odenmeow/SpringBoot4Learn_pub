package com.oni.training.springboot.WebCrawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileWriter;

public class WebPageDownloaderWithSelenium {
    public static void main(String[] args) {
        // The driver executable must exist: C:\Users\qw284\IdeaProjects\springboot\chromedriver 我是被說應該在這下面有
        // 使用類別加載 取得資源位置 因為我放在Resources那邊，不然妳就去試著看說應該放哪
        ClassLoader classLoader = WebPageDownloaderWithSelenium.class.getClassLoader();
        String chromeDriverPath = classLoader.getResource("chromedriver.exe").getPath();
        // 設定 ChromeDriver 的路徑
//        System.setProperty("webdriver.chrome.driver", "chromedriver");
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        // 初始化 Chrome 瀏覽器
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 以無頭模式運行瀏覽器（不顯示 GUI）
        WebDriver driver = new ChromeDriver(options);

        try {
            // 訪問網頁
            driver.get("https://zh.wikipedia.org/zh-tw/%E7%B6%B2%E8%B7%AF%E7%88%AC%E8%9F%B2");

            // 等待一些時間，以確保 JavaScript 有足夠的時間生成內容
            Thread.sleep(2000);

            // 取得網頁內容
            String pageSource = driver.getPageSource();

            // 將網頁內容保存到本地檔案
            FileWriter writer = new FileWriter("webpage.html");
            writer.write(pageSource);
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 關閉瀏覽器
            driver.quit();
        }
    }
}

