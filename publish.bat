@echo off
setlocal

set "MAVEN_HOME=C:\Users\praharsha.k\AppData\Local\Programs\SimplifyQA-CodeEditor\resources\app\resources\maven\bin"
set "JAVA_HOME=C:\Users\praharsha.k\AppData\Local\Programs\SimplifyQA-CodeEditor\resources\app\resources\jdk"

set "PATH=%JAVA_HOME%;%MAVEN_HOME%;%PATH%"

reg add "HKCU\Console" /v VirtualTerminalLevel /t REG_DWORD /d 1 /f >nul 2>&1
echo [1m[33m=============================== STARTING PUBLISH PROCESS ===============================[0m


call mvn compile exec:java -Ppublish
set "EXIT_CODE=%ERRORLEVEL%"

if %EXIT_CODE% NEQ 0 (
    echo [1m[31m=============================== PUBLISH FAILED =====================================[0m
    echo [1m[36m=============================== REFER THE LOGS =========================================[0m
) else (
    echo [1m[32m============================================== PUBLISH SUCCESSFUL ===============================================[0m
)

endlocal
