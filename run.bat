@echo off
REM Build and run the Codebase Analyzer

echo Building project...
call mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo Build failed.
    exit /b %ERRORLEVEL%
)

echo Running Analyzer...
java -jar target\containers-1.0-SNAPSHOT.jar %*
