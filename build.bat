@echo off
REM Gaming Hub - Compilation and Run Script for Windows

echo ================================
echo Gaming Hub - Build ^& Run Script
echo ================================
echo.

REM Check if SQLite JDBC driver exists
if not exist "sqlite-jdbc-3.43.0.0.jar" (
    echo Downloading SQLite JDBC driver...
    powershell -Command "(New-Object System.Net.ServicePointManager).SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; (New-Object System.Net.WebClient).DownloadFile('https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar', 'sqlite-jdbc-3.43.0.0.jar')"
    if errorlevel 1 (
        echo Failed to download SQLite JDBC driver
        echo Please download manually from: https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar
        exit /b 1
    )
)

REM Create bin directory if it doesn't exist
if not exist "bin" (
    echo Creating bin directory...
    mkdir bin
)

REM Compile
echo Compiling Java files...
REM Find all Java files and compile them
for /r . %%f in (*.java) do (
    echo Compiling %%f
)
javac -cp sqlite-jdbc-3.43.0.0.jar -d bin -sourcepath . ^
    Main.java ^
    models\User.java ^
    models\GameScore.java ^
    database\DatabaseManager.java ^
    database\UserDAO.java ^
    database\GameScoreDAO.java ^
    services\AuthenticationService.java ^
    gui\LoginPage.java ^
    gui\RegisterPage.java ^
    gui\Dashboard.java ^
    games\TicTacToeGame.java ^
    games\SnakeGame.java ^
    games\NumberGuessingGame.java

if errorlevel 1 (
    echo Compilation failed!
    exit /b 1
)

echo Compilation successful!
echo.
echo Starting Gaming Hub...
echo.

REM Run
cd bin
java -cp .;..\sqlite-jdbc-3.43.0.0.jar Main
cd ..
