@echo off
chcp 65001 >nul
title Backend Service
echo ==========================================
echo 正在启动后端服务 (Spring Boot)...
echo 请勿关闭此窗口
echo ==========================================

REM 强制设置 JAVA_HOME 为 JDK 25
set "JAVA_HOME=C:\Program Files\Java25"

set "SCRIPT_DIR=%~dp0"
set "SETTINGS=%SCRIPT_DIR%..\settings.xml"
call "C:\apache-maven-3.9.11\bin\mvn.cmd" spring-boot:run -s "%SETTINGS%" -f "%SCRIPT_DIR%pom.xml"
pause
