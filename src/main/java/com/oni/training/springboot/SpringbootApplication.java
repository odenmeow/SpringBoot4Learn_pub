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
// ctrl+shift +  (+/-)  展開或收攏到只剩方法標題名稱
// alt+shift +  (+/-)   最小只能100%  不能往下QQ


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

// git使用 idea (intellij idea) JetBrain的軟體

// Terminal打開 然後【因為尚未使用git】所以可輸入
// git init 初始化倉庫
// git add . 將您的專案檔添加到Git的暫存區。這將跟蹤專案檔的更改。
// git commit -m "提交消息描述"
// 使用以下命令將您的本地倉庫與GitHub倉庫關聯（請確保您已登錄GitHub並具有適當的許可權）：
// 		git remote add origin https://github.com/odenmeow/SpringBoot4Learn.git
// 使用以下命令將您的提交推送到GitHub倉庫：
// 		git push -u origin master
//		這將把您的本地分支（通常是“master”分支）推送到GitHub倉庫。


// 一旦完成上述步驟，您的項目檔將被推送到GitHub倉庫中。
// 如果這是您首次推送，您可能需要提供GitHub憑據進行身份驗證。
// 請注意，這些步驟假定您已在本地安裝了Git並設置了GitHub帳戶。
// 確保您在執行這些命令之前已經登錄GitHub，並具有推送到倉庫的許可權。

// git只要去下載就好 然後一樣加入環境變數就可以這樣玩了


// git 你在這一航旁邊數字上方點右鍵 並解打開annotate git blame就可以看到是誰編輯的了

// git log 可以查看所有提交歷史commit部分 回去任意commit
// git checkout <commit-SHA> 回去任意commit跟branch 不同唷
// ✔【很重要所以括號】
//      回到commitA 的話 master 所有在commitA後多新增的file、folder 不會消失
//		會改變上次提交的內容 (回到上次的修正)而已

// 這邊教你怎麼新增並push branch 分支
// 如果下面兩個步驟之前沒有使用 git add. commit push 那麼會是前一個commit的快照
// git branch Chapter8  左邊是章節8的意思  (因為我要依照章節做切換)
// git push origin Chapter8 這樣就能推送上去

// 查看分支
// PS   C:\Users\qw284\IdeaProjects\springboot> git branch
//		Chapter8
//		* master
//		PS C:\Users\qw284\IdeaProjects\springboot>

// 【使用分支】
// git checkout Chapter8

// 【刪除分支 本地+遠端分支都要刪除喔】
// git branch -d branch-name / git branch -D branch-name
// git push origin --delete Chapter8


// git pull 對於在commitA 後刪除某資料 並不會復原
// 【依照關鍵字查詢】
// git log --grep="新增"  # 顯示包含 "bug fix" 關鍵字的提交
// 【依照時間查詢】
// git log --since="2 weeks ago" --until="yesterday"  # 顯示過去兩週內的提交
// 【恢復某個特定文件】
// git checkout commitA -- path/to/file
// 【小心使用、拋棄目前所有變更強制跳回某版本、此招可以復原空文件】
// git reset --hard commitA














// 在 Windows 系統上：
//true 表示在推送時轉成 ，在拉取時轉成 \n\r\n。这样的设置让 Windows 的开发者能兼容很多的开发工具（比如早期的记事本，新的已经支持 \r\n 了），不至于遇到很多换行符问题。
//false 表示在推送时和拉取时都原样保留换行符。这样的设置在所有程序员都在同一个平台开发时很有用，git 完全不处理换行符，全部改由开发者自行解决。
//input 表示在推送时转成 \n，在拉取时原样保留换行符。注意到，这样的设置会让仓库里所有的换行符都变成 \n 不再有什么时候有 \r\n 了，所以对 Windows 平台的开发者并不友好。


//Q: 所以 可能移植=input 不想動到任何可能格式=false 預設windows=true
//A: 是的，你可以這樣總結不同的設置：core.autocrlf
//core.autocrlf = true（預設值，適用於 Windows）：在提交時將換行符轉換為CRLF（回車換行），在檢出時將CRLF轉換為適應平臺的LF（僅適用於Windows），適合在 Windows 上開發，但可能導致在其他平臺上出現換行符問題。
// 													<<<因為僅幫windows轉換所以其他平台會壞掉>>>
//core.autocrlf = false：不執行任何換行符轉換，適用於不希望在不同平台之間進行自動換行符轉換的情況。這在維護代碼的一致性和可移植性方面很有用，但需要手動管理換行符。
//													<<<				兼容				>>>
//core.autocrlf = input（適用於Linux和macOS，也適用於Windows）：在提交時將換行符轉換為LF（僅適用於Windows），在檢出時不執行轉換，適用於在Windows上開發並確保在不同平臺上保持一致的換行符風格。
//													<<< 	不變更 假定只在同一個編譯器用>>>
//所以，如果你在 Windows 上進行開發，但不想在不同平台之間處理換行符問題，可以將設置為。如果你希望代碼能夠在不同平臺上正確處理換行符，可以將其設置為。最重要的是，根據你的需要選擇合適的設置。core.autocrlffalseinput


// =======使用   git config --get core.autocrlf 可以看到 目前是?

