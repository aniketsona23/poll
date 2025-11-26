@echo off
setlocal EnableDelayedExpansion

REM ==========================================
REM   Windows Batch Script for Codebase Analyzer
REM ==========================================

set SRC_DIR=src
set BIN_DIR=build
set LIB_DIR=lib
set MAIN_CLASS=com.analyzer.cli.Main
set JAR_FILE=codebase-analyzer.jar

REM Adjust versions to match your lib folder
set JUNIT_JAR=%LIB_DIR%\junit-platform-console-standalone-1.10.2.jar
set JAVAPARSER_JAR=%LIB_DIR%\javaparser-core-3.26.0.jar
set GSON_JAR=%LIB_DIR%\gson-2.11.0.jar

REM Classpath for execution
set RUN_CP=%JAR_FILE%;%JAVAPARSER_JAR%;%GSON_JAR%
set BUILD_CP=%JAVAPARSER_JAR%;%GSON_JAR%;%JUNIT_JAR%

REM Command routing
if "%1"=="" goto repl
if "%1"=="build" goto build
if "%1"=="clean" goto clean
if "%1"=="test" goto test
if "%1"=="repl" goto repl
if "%1"=="help" goto help

REM If not a special command, assume it's a CLI command (analyze, list, etc.)
goto exec

:help
echo Codebase Analyzer - Windows Runner
echo.
echo Usage:
echo   run.bat build             - Compile the project
echo   run.bat clean             - Remove build artifacts
echo   run.bat test              - Run JUnit tests
echo   run.bat repl              - Start interactive mode
echo   run.bat [command] [args]  - Run specific command
echo.
echo Examples:
echo   run.bat analyze --path .\src
echo   run.bat list classes
goto end

:clean
echo Cleaning build artifacts...
if exist %BIN_DIR% rmdir /s /q %BIN_DIR%
if exist %JAR_FILE% del /q %JAR_FILE%
if exist sources.txt del /q sources.txt
if exist manifest.txt del /q manifest.txt
echo Done.
goto end

:build
echo Compiling Java source...
if not exist %BIN_DIR% mkdir %BIN_DIR%

REM Find all java files
dir /s /b %SRC_DIR%\*.java > sources.txt

REM Compile
javac -cp "%BUILD_CP%" -d %BIN_DIR% @sources.txt
if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    goto end
)

REM Create JAR
echo Main-Class: %MAIN_CLASS% > manifest.txt
jar cfm %JAR_FILE% manifest.txt -C %BIN_DIR% .
echo Build complete. JAR created: %JAR_FILE%
goto end

:test
call :build
echo Running JUnit tests...
java -jar "%JUNIT_JAR%" --class-path "%BIN_DIR%;%JAVAPARSER_JAR%;%GSON_JAR%" --scan-class-path
goto end

:repl
call :build
echo Starting REPL...
java -cp "%RUN_CP%" %MAIN_CLASS% repl
goto end

:exec
call :build
REM Pass all arguments to the main class
java -cp "%RUN_CP%" %MAIN_CLASS% %*
goto end

:end
endlocal
