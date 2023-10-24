package com.oni.training.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
public class
SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);

	}

}
// ***請不要 選取反白文字後，CTRL+S 可能有BUG 東西消失 很麻煩喔
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
// end / fn+左或右邊 行尾 行首

// run之後會出現target 裡面是你的byte code 。
// 對應影片https://www.youtube.com/watch?v=XCqVCq249Iw
// alt+` (~)    (自己選擇要不comment) VCS enable version control integeration
// ctrl+k 可以去做git類似 comment提交的 (大視窗)
// alt+9 叫出 log (Git)的
// ctrl+alt+l 自動格式 相當於ctrl+shift+f (eclipse)
// Ctrl+W expand 選取範圍
// Editor > Inlay hint 關閉 提示



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

/**   G    i    t
 * 		預設會忽略空的folder file 所以建議 folder內要放file然後打字 才會被加入!
 * */
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
//      -u是關聯 以後只要push就好( 如果有多個remote add a/b/c )
//      以後默認就是 push到 origin master ( 如果其他執行的時候沒有使用 -u a/b/c master)
//			【也不是很重要 以後用到應該就會了拉 反正加減看】

/** git reset --soft HEAD~1  更改commit消息(僅適用本地端尚未push想修改上一步驟的消息)*/
// 一旦完成上述步驟，您的項目檔將被推送到GitHub倉庫中。
// 如果這是您首次推送，您可能需要提供GitHub憑據進行身份驗證。
// 請注意，這些步驟假定您已在本地安裝了Git並設置了GitHub帳戶。
// 確保您在執行這些命令之前已經登錄GitHub，並具有推送到倉庫的許可權。

// git只要去下載就好 然後一樣加入環境變數就可以這樣玩了
/** 移除遠方remote 關聯名稱*/
// 【git】 git remote remove origin
/** 關於git init後會產生隱藏的 .git 可以手動也可以命令刪除*/
// 【linux】 rm -rf .git
// 【windows】Remove-Item -Recurse -Force .git
//  關於這刪除 請小心使用 因為可能遞迴 如果沒打好可能全部不見
/***  【 以下是怎麼把遠方的資料 抓到自己本台電腦的某資料夾 下方 然後進行使用】
 PS C:\_JSP\spring-workspaceA> Remove-Item -Recurse -Force .git 強制刪除
 PS C:\_JSP\spring-workspaceA> git branch  		找不到因為沒有init
		fatal: not a git repository (or any of the parent directories): .git
 PS C:\_JSP\spring-workspaceA> git init			初始化
 		Initialized empty Git repository in C:/_JSP/spring-workspaceA/.git/
 PS C:\_JSP\spring-workspaceA> git clone https://github.com/odenmeow/SpringHibernate/
		Cloning into 'SpringHibernate'...				複製過來目前的資料夾
		remote: Enumerating objects: 1539, done.
		remote: Counting objects: 100% (1539/1539), done.
		remote: Compressing objects: 100% (1084/1084), done.
		remote: Total 1539 (delta 401), reused 1486 (delta 348), pack-reused Receiving objects:  99% (1524/1539), 1.70 MiB | 844.00 KiB/ss
		Receiving objects: 100% (1539/1539), 2.26 MiB | 925.00 KiB/s, done.
		Resolving deltas: 100% (401/401), done.
 PS C:\_JSP\spring-workspaceA> git branch -r					你複製過來但是你看不到遠端喔
 PS C:\_JSP\spring-workspaceA> git branch -a					一樣看不到啦
 PS C:\_JSP\spring-workspaceA> cd SpringHibernate 				進去裡面才有!
 PS C:\_JSP\spring-workspaceA\SpringHibernate> git branch -r   遠方也有了!
 		origin/HEAD -> origin/SpringExampleMM22
 		origin/HibernateExamplesMM23
 		origin/SpringExampleMM22
 		origin/master
 PS C:\_JSP\spring-workspaceA\SpringHibernate> git branch  		 突然有了! 所以資料是藏這裡面!
		* SpringExampleMM22
 PS C:\_JSP\spring-workspaceA\SpringHibernate> git switch master  切換分支
 		Switched to a new branch 'master'
 		branch 'master' set up to track 'origin/master'.
 PS C:\_JSP\spring-workspaceA\SpringHibernate> git branch		切換本地才會 顯示
 		SpringExampleMM22
 		* master





 PS C:\_JSP\spring-workspaceA\SpringHibernate> git remote -v   查看遠方的 別名 不需要用連接也可pull
 		origin  https://github.com/odenmeow/SpringHibernate/ (fetch)
 		origin  https://github.com/odenmeow/SpringHibernate/ (push)
 PS C:\_JSP\spring-workspaceA\SpringHibernate> git remote
 		origin

**/

// 【checkout  switch通用 但是最好用新的switch】

// git 你在這一航旁邊數字上方點右鍵 並解打開annotate git blame就可以看到是誰編輯的了
// git log 可以查看所有提交歷史commit部分 回去任意commit
// git checkout <commit-SHA> 回去任意commit跟branch 不同唷
// ✔【很重要所以括號】
//      回到commitA 的話 master 所有在commitA後多新增的file、folder 不會消失
//		會改變上次提交的內容 (回到上次的修正)而已

// 【新增並push branch 分支】
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
// 【分析為什麼pull沒東西】
//		因為刪除後沒有提交 所以pull以為是同一個版本就沒做事情!
// 【如果怕pull發生衝突請選用以下】
// git fetch
// {詳細再請自己查看暫時沒用過，好像說有發生衝突會顯示 ，得自己手動解決<但是相對更安全>	}

// 【如果無法切換分支 也確定目前資料夾的非git文件都不需要 則使用這個命令】
// git clean -f -d
// 【複製對面Repository】
// git clone https://github.com/odenmeow/SpringHibernate
// 隨便找一個 新專案 開啟shell然後輸入  ，會發現 不是這樣用拉~。
// 以下才是正確方式:

// 		1.先從Terminal切換到管理專案的那個 file
// 			例如C:\_JSP\spring-workspaceA\SpringHibernate
//			的 C:\_JSP\spring-workspaceA
//		2.把檔案clone下來 git clone https://github.com/odenmeow/SpringHibernate
//			這邊只會git默認的那個branch 並不會把所有都抓下來
//			透過import 去找到這個檔案位置 在eclipse之類 打開專案以查看
//		3.如果這時候想透過 git branch HibernateExamplesMM23 會失敗 因為要先fetch
//			但是其實不強制 這個失敗是有變更未保存 怕你壞事
//			git checkout -f HibernateExamplesMM23 好像可以強制抓遠端資料
//   ✔		為什麼使用fetch 是為了保證是去抓新的 否則如果本地端有branch會直接用舊的!
//
/**	使用fetch : 搭配 stash 比較安全*/
//		【使用情境:	覺得有可能衝突的話】
//		fetch 會把遠端倉儲所有資料、分支暫時抓下來
//		git stash 可以保存【當前分支】的內容
//		必須一個個切換branch並使用stash保證等會的fetch 覆蓋後 有機會處理問題
//
/***【做任何ˊ事情之前最好都先commit、push 完畢了在做 避免不小心覆蓋就沒了! 】 **/







// 透過 【git branch -a】
//		C:\_JSP\spring-workspaceA\SpringHibernate>git branch -a
//		* SpringExampleMM22								是你的本地分支
//		remotes/origin/HEAD -> origin/SpringExampleMM22	指向遠端儲存庫的預設分支 。SpringExampleMM22
//		remotes/origin/HibernateExamplesMM23			是遠端儲存庫中的一個分支
//		remotes/origin/SpringExampleMM22				是遠端儲存庫中的一個分支
//		remotes/origin/master							是遠端儲存庫中的一個分支



/**不小心弄錯了push覆蓋上去了(剛好沒衝突)*/

// 是的，您可以使用以下步驟來糾正不小心將分支 A 推送到分支 B 的情況：
//
//	使用 或 尋找分支 B 上一次正常的提交的哈希值，假設為 。git refloggit log<commit-hash>
//
//	使用 將本地分支 B 重置到正確的提交。 這將使您的本地分支 B 恢復到正確的狀態，但尚未推送到遠端倉庫。git reset --hard <commit-hash>
//
//	使用 將本地分支 B 強制推送到遠端倉庫的分支 B。 這將覆蓋遠端分支 B 上的舊提交，確保它與您的本地分支一致。git push --force origin branch-B
//
//	請務必小心使用強制推送，確保您了解其潛在影響，以免不小心刪除或覆蓋重要的提交。



/**想要退一個版本 或者 把剛剛做的 還沒commit的事物都刪除*/

// 【建議先stage 需要的資料 push之後 再把不要的刪除】

//  git reset --hard HEAD~1   #退一版commit 到 還沒修改前
//                                        #HEAD~1 每執行一次一次退一版commit
//  git clean -f -d   #移除未加入版控的檔案(-f) & 目錄(-d)  *重要*
//  git pull             #重拉remote目前版本

/** 已經push上去了 但是想要撤銷剛剛 push並且 更改commit內容 但是不想回歸上一個版本**/

// git add .
// git commit --amend -m "內容"      (這邊會撤銷上一個commit 以這一個為主 並且不會讓目前檔案消失)
// git push --force origin master


/** 特殊  A (想回到不只前一個版本 怎麼辦 commit連續兩次忘記打關鍵字)*/
// 目前最新是 a  b  c   前兩個commit 訊息都不要 但內容要保存
// 將分支的 HEAD 移動到 commit c，保留工作目錄和暫存區中的更改
// 		git reset --soft commit_c	或者
// 		git reset --soft HEAD~2
// 提交從 commit a 到 commit c 的所有更改
// 		git commit -m "Reapply changes from commit a to commit c"

//  {額外提醒，如果回到過去不小心做錯建議先回去做錯之前的版本 再度pull}
//  {因為如果回歸後又做錯了 使用HEAD~2  也不會回到c 當下會很亂。}
//  git reset --hard  coomit_hash
//  git pull    (回到最後提交push至遠端狀態)



// git reset 命令中的 --soft和--mixed 選項之間的主要區別在於它們對工作目錄和暫存區的影響：--soft--mixed
// git reset --soft：此選項將分支的 HEAD 放在指定的提交，但保留工作目錄和暫存區中的更改。 這意味著您可以重新提交這些更改，而不需要重新編輯它們。 這個選項通常用於撤銷一些提交並重新整理提交歷史，同時保留更改以進行進一步的工作。
// git reset --mixed（或簡寫為 git reset，不帶 --soft 或 --hard 選項）：這是預設行為。 它將分支的 HEAD 放在指定的提交，但會重置暫存區，因此工作目錄中的更改仍然存在，但不在暫存區中。 這意味著您需要重新將更改添加到暫存區，然後提交它們。
// 所以， --soft 和 --mixed的不同之處在於是否重置暫存區。 如果您想保留更改並將它們包含在下一次提交中，您可以使用 --soft 選項。 如果您只想將更改保留在工作目錄中但不包含在下一次提交中，您可以使用預設的--mixed選項。

/** 特殊  B (想回到不只前一個版本 怎麼辦 commit連續兩次忘記打關鍵字)*/

//  { 不使用soft 變動資料不會存在'暫存區' commit 也不會紀錄 需要自己add .  }
//  使用stash 操作 更靈活控制!



// git cherry-pick  蠻特殊可以把別人的commit 合併給自己~ 自己去查怎麼用



/** 特殊 (想回到不只前一個版本 怎麼辦 commit連續兩次忘記打關鍵字)*/
// 目前最新是 a  b  c   前兩個commit 訊息都不要 但內容要保存
// 將分支的 HEAD 移動到 commit c，保留工作目錄和暫存區中的更改
// 		git reset --soft commit_c	或者
// 		git reset --soft HEAD~2
// 提交從 commit a 到 commit c 的所有更改
// 		git commit -m "Reapply changes from commit a to commit c"

//  {額外提醒，如果回到過去不小心做錯建議先回去做錯之前的版本 再度pull}
//  {因為如果回歸後又做錯了 使用HEAD~2  也不會回到c 當下會很亂。}
//  git reset --hard  coomit_hash
//  git pull    (回到最後提交push至遠端狀態)

/**  {       開大招      }*/
// git reset --soft HEAD~2
// git stash save -m "保存c~a的更改"    -> 存到特殊空間 而不是staged空間
//  <剛好發現我的 這個commit 訊息也想改 但 不要跟後續內容放一起 而是純改訊息>
//  {c~a都放到暫存空間了 不怕commit了}
// git commit --amend -m "CH14 > 越來越不同了，我沒有像他那樣設定多個MailService 然後或者用@Qualifier或者用配置properties去改，我唯一跟他做的只有Service弄成Config的bean"
// PS C:\Users\qw284\IdeaProjects\springboot> git stash list
// stash@{0}: On master: 保存c~a的更改
// git stash pop --index 0    填入 0 就可以



// git reset 命令中的 --soft和--mixed 選項之間的主要區別在於它們對工作目錄和暫存區的影響：--soft--mixed
// git reset --soft：此選項將分支的 HEAD 放在指定的提交，但保留工作目錄和暫存區中的更改。 這意味著您可以重新提交這些更改，而不需要重新編輯它們。 這個選項通常用於撤銷一些提交並重新整理提交歷史，同時保留更改以進行進一步的工作。
// git reset --mixed（或簡寫為 git reset，不帶 --soft 或 --hard 選項）：這是預設行為。 它將分支的 HEAD 放在指定的提交，但會重置暫存區，因此工作目錄中的更改仍然存在，但不在暫存區中。 這意味著您需要重新將更改添加到暫存區，然後提交它們。
// 所以， --soft 和 --mixed的不同之處在於是否重置暫存區。 如果您想保留更改並將它們包含在下一次提交中，您可以使用 --soft 選項。 如果您只想將更改保留在工作目錄中但不包含在下一次提交中，您可以使用預設的--mixed選項。


// git cherry-pick  蠻特殊可以把別人的commit 合併給自己~ 自己去查怎麼用

/** 建立空白的專案 然後 做成遠端某repo的分支**/


//		git init
//		git remote add origin http....
//		git fetch                       關聯但不pull
//		git branch -a                   可以看到了
//		接著先 commit	但不要 push 這樣會建立一個初始的主幹
// 		git add .
// 		git commit -m "Initialize."
//		.....
//		...
//		.		接著可以開始建立分支了
//		git branch SpringSecurityLearn
//		git switch SpringSecurityLearn
//      git branch 和  git branch -a 	自己看
//		接著就等著做得差不多==>上傳到 origin 新增分支過去給它 (SpringBoot4Learn) 玩!

/** 	Stash 使用	*/

//  	git stash -u	檔案未 staged +unStaged +untracked 	都 	放到特殊空間
//		git stash 		staged 的檔案 							放到特殊空間
//		git stash pop   FILO

//		staged   	= add .
//		unStaged 	= 本來就被	git 追蹤的檔案，今天有被修改 還沒有 staged    (需要透過git add file) 讓它 staged
//      untracked	= 全新的檔案 	git 還不知道 、沒追蹤過



/**		git帳號切換			*/
// window 管理電腦憑證  直接輸入憑證就會找到  然後
// 如果找不到 可能不是存在那邊，試著用 git config credential.helper 看是不是在store=git內建
//	C:\Umimi>git credential reject
//			protocol=https
//			host=github.com
//	透過上述就可以 刪除憑證了
//	If user.email=oni@gmail.com 雖然登入github用Umi帳號
//	push 後，Github上Repository 顯示oni上傳

//  Credential 跟 作者是分開的 (蠻神奇的)


// 下面不要在意，這無法解決credential問題 還是需要從windows去刪除
		//	git config --global --list > oni.txt     	保存在cmd 使用這個指令的路徑

		//	Global user資料刪除
		//	git config --global --unset user.name
		//	git config --global --unset user.email

		//  當前 cmd git repo 刪除 個人資料
		//	git config --unset user.name
		//  git config --unset user.email

		//  手動一個一個恢復

		//	git config --global user.email "qw28425382694@gmail.com"
		//	git config --global user.name "Oni"

		//	git config --global --file /path/to/your/backup/git_config_backup.txt



/**		到這邊上方 都是   G i t		*/










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

