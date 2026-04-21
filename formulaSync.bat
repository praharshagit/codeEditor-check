@echo off
setlocal

set "MAVEN_HOME=C:\Users\praharsha.k\AppData\Local\Programs\SimplifyQA-CodeEditor\resources\app\resources\maven\bin"
set "JAVA_HOME=C:\Users\praharsha.k\AppData\Local\Programs\SimplifyQA-CodeEditor\resources\app\resources\jdk"

set "PATH=%JAVA_HOME%;%MAVEN_HOME%;%PATH%"

reg add "HKCU\Console" /v VirtualTerminalLevel /t REG_DWORD /d 1 /f >nul 2>&1
echo [1m[33m=============================== STARTING FORMULA SYNC PROCESS ===============================[0m


call mvn compile exec:java -PsyncFormula
set "EXIT_CODE=%ERRORLEVEL%"

if %EXIT_CODE% EQU 0 (
    echo [1m[32m================================ SYNC CUSTOM FORMULA REQUEST SUCCESSFULL ===============================[0m
) else (
    echo [1m[31m============================ COULD NOT SYNC CUSTOM FORMULA.. REFER ABOVE ERROR/INFO IN LOGS ====================================[0m
)

endlocal
