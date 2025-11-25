@echo off
REM Git Commit Log Analyzer - Windows Build Script

if "%1"=="" goto help
if "%1"=="help" goto help
if "%1"=="compile" goto compile
if "%1"=="analyzer" goto analyzer
if "%1"=="test-all" goto test-all
if "%1"=="clean" goto clean
goto unknown

:help
echo ================================================================
echo   PoPL Quiz - Build Script
echo ================================================================
echo.
echo Available commands:
echo   build.bat compile    - Compile Git Commit Log Analyzer
echo   build.bat analyzer   - Run Git Commit Log Analyzer (CLI)
echo   build.bat test-all   - Run all Maven tests
echo   build.bat clean      - Clean all build artifacts
echo   build.bat help       - Show this help message
echo.
echo Git Analyzer Examples:
echo   build.bat compile    # Compile the analyzer
echo   build.bat analyzer   # Start interactive CLI
echo.
echo Maven Test Examples:
echo   build.bat test-all   # Run all tests
echo.
goto end

:compile
echo Compiling Git Commit Log Analyzer...
if not exist bin mkdir bin
javac -d bin src\main\java\com\containers\*.java src\main\java\com\myorg\gitinsights\*.java
if errorlevel 1 (
    echo ERROR: Compilation failed!
    exit /b 1
)
echo.
echo *** Compilation successful! ***
echo.
echo To run the analyzer, use: build.bat analyzer
goto end

:analyzer
echo Starting Git Commit Log Analyzer...
if not exist bin (
    echo ERROR: bin directory not found. Run 'build.bat compile' first.
    exit /b 1
)
echo.
java -cp bin com.myorg.gitinsights.Main
goto end

:test-all
echo Running all Maven tests...
call mvn clean test
goto end

:clean
echo Cleaning build artifacts...
if exist bin (
    rmdir /s /q bin
    echo Deleted bin directory
)
call mvn clean
echo.
echo *** Clean complete! ***
goto end

:unknown
echo Unknown command: %1
echo Run 'build.bat help' for available commands.
exit /b 1

:end
