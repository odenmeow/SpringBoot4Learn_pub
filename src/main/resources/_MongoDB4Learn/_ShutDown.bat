@echo off
:: 提示用戶確認關機
set /p user_input=shutdown(Y/N)-case insensitive:

:: 檢查用戶輸入是否為 "是"（不區分大小寫）
if /i "%user_input%"=="Y" (
    docker stop mongodb
	docker stop mongo-express
    docker stop mytutu
    shutdown /s /t 5
) else (
    echo Canceled
    pause
)
