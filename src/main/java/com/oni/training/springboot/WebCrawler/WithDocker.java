package com.oni.training.springboot.WebCrawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

// 寫一下好了 以下用以安裝docker讓selenium可以去跑 chromdriver 需要的低版本chrome
// $ docker pull selenium/standalone-chrome
// $ docker run -d -p 4444:4444 --shm-size=2g selenium/standalone-chrome
public class WithDocker {
    public static void main(String[] args) throws MalformedURLException {
        // The driver executable must exist: C:\Users\qw284\IdeaProjects\springboot\chromedriver 我是被說應該在這下面有
        // 使用類別加載 取得資源位置 因為我放在Resources那邊，不然妳就去試著看說應該放哪
        ClassLoader classLoader = WebPageDownloaderWithSelenium.class.getClassLoader();
        String chromeDriverPath = classLoader.getResource("chromedriver.exe").getPath();
        // 设置ChromeDriver路径
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        // 添加浏览器选项，例如设置代理、启用/禁用浏览器扩展等
        options.addArguments("--disable-extensions");
        // 创建一个ChromeDriver实例，连接到远程的Selenium服务器
        WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);


        // 访问网页
        driver.get("https://www.goodjob.life/experiences/637ba759c362100012537346");

        String pageSource = driver.getPageSource();

        // 创建一个目录来保存图片，如果目录不存在的话
        File imagesDirectory = new File("images");
        if (!imagesDirectory.exists()) {
            imagesDirectory.mkdir();
        }

        // 获取页面中的所有图片元素
        List<WebElement> imageElements = driver.findElements(By.tagName("img"));
        for (WebElement imageElement : imageElements) {
            String imageUrl = imageElement.getAttribute("src");
            downloadImage(imageUrl, "images"); // 下载图片并保存在 "images" 目录下
        }

        // 将页面内容保存为 HTML 文件，保存在与Java类文件相同的位置
        try (PrintWriter writer = new PrintWriter("page.html", "UTF-8")) {
            writer.println(pageSource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭浏览器
        driver.quit();
    }

    // 下载图片并保存在指定目录
    private static void downloadImage(String imageUrl, String directory) {
        try (InputStream in = new URL(imageUrl).openStream();
             OutputStream out = new FileOutputStream(directory + "/" + getFileName(imageUrl))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取图片文件名
    private static String getFileName(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }

}
