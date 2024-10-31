@echo off
setlocal

set SRC_DIR=src\main\java
set OUTPUT_DIR=out
set MAIN_CLASS=ecosystem.EcosystemSimulator

if not exist %OUTPUT_DIR% (
    mkdir %OUTPUT_DIR%
)

echo Compiling Java files...
for /R %SRC_DIR% %%f in (*.java) do (
    javac -d %OUTPUT_DIR% -sourcepath %SRC_DIR% "%%f"
)

if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)

echo Running the Java program...
java -cp %OUTPUT_DIR% %MAIN_CLASS%

pause