package com.oni.training.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}

}

// Intellj 下載的jdk 他們放在qw284 的 .jdk
// sout = syso
// fn+insert+alt= ctl+n 創新 class 或者建構式也可以用
// ctrl+shift+enter = 自動結束跳到後面+上分號;
// alt+1  指標跑到左邊去 可以讓選擇指向當。前檔案
// fn+ctrl+f12  可以打開大綱 = eclipse ctrl+o
// shift+F10 run (java program)
// shift 兩次 可以叫出小介面 (go to test=ctrl+shift+t)
// ctrl+y 刪除當行
// ctrl+F12=   eclipse (ctrl+o)  自動Import之類
// shift+F6 = refactor rename
// 對著資料夾使用Mark as Directory 可以把包com example.demo  合併成com.example.demo 之類  ( 出問題可使用 )



// run之後會出現target 裡面是你的byte code 。
// 對應影片https://www.youtube.com/watch?v=XCqVCq249Iw
// alt+` (~)    (自己選擇要不comment) VCS enable version control integeration
// ctrl+k 可以去做git類似 comment提交的 (大視窗)
// alt+9 叫出 log (Git)的
// ctrl+alt+l 自動格式 相當於ctrl+shift+f (eclipse)
// Ctrl+W expand 選取範圍

//	Ctrl + Alt + V。 introduce to local variable
//	@GetMapping
//	public String Hello() {
//		return "hello"; // localhost:8080 才看得到喔
//	}
//	@GetMapping
//	public List<String> Hello() {
//		System.out.println(List.of("Hello","World"));
//		return List.of("Hello","World"); // localhost:8080 才看得到喔
//	}
//	@GetMapping
//	public List<Student> Hello() {
//		return List.of(
//				new Student(
//						1L,
//						"Yumi",
//						"umi123.@gmail.com",
//						LocalDate.of(2000, Month.JANUARY,5),
//						21
//				)
//		); // localhost:8080 才看得到喔
//		//edge://flags/   裡面可以去改json
//	}
//  java -jar .\demo-0.0.1-SNAPSHOT.jar --server.port=8081

