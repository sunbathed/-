@echo off
chcp 65001 >nul
title Frontend Service
echo ==========================================
echo 正在启动前端服务 (Vue3)...
echo 请勿关闭此窗口
echo ==========================================
if not exist node_modules call npm install
npm run dev
pause