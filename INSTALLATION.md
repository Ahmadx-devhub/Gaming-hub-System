# Installation & Compilation Guide

## Platform-Specific Instructions

### 🍎 macOS

#### Step 1: Download SQLite Driver

```bash
cd /Users/apple/Desktop/projects/java_project
wget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar
```

Or using curl:

```bash
curl -o sqlite-jdbc-3.43.0.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar
```

#### Step 2: Make build script executable & Run

```bash
chmod +x build.sh
./build.sh
```

#### Step 3: Manual Compilation (if needed)

```bash
mkdir -p bin
javac -cp sqlite-jdbc-3.43.0.0.jar -d bin $(find . -name "*.java")
```

#### Step 4: Manual Run (if needed)

```bash
cd bin
java -cp .:../sqlite-jdbc-3.43.0.0.jar Main
cd ..
```

---

### 🐧 Linux

#### Step 1: Download SQLite Driver

```bash
cd /Users/apple/Desktop/projects/java_project
wget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar
```

#### Step 2: Make build script executable & Run

```bash
chmod +x build.sh
./build.sh
```

#### Step 3: Manual Compilation (if needed)

```bash
mkdir -p bin
javac -cp sqlite-jdbc-3.43.0.0.jar -d bin $(find . -name "*.java")
```

#### Step 4: Manual Run (if needed)

```bash
cd bin
java -cp .:../sqlite-jdbc-3.43.0.0.jar Main
cd ..
```

---

### 🪟 Windows (Command Prompt)

#### Step 1: Download SQLite Driver

Use PowerShell:

```powershell
cd C:\Users\YourUsername\Desktop\projects\java_project
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar" -OutFile "sqlite-jdbc-3.43.0.0.jar"
```

Or download manually from:
https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar

#### Step 2: Run build script

```batch
build.bat
```

#### Step 3: Manual Compilation (if needed)

```batch
mkdir bin
javac -cp sqlite-jdbc-3.43.0.0.jar -d bin ^
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
```

#### Step 4: Manual Run (if needed)

```batch
cd bin
java -cp .;..\sqlite-jdbc-3.43.0.0.jar Main
cd ..
```

---

### 🪟 Windows (PowerShell)

#### Step 1: Download SQLite Driver

```powershell
cd C:\Users\YourUsername\Desktop\projects\java_project
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar" -OutFile "sqlite-jdbc-3.43.0.0.jar"
```

#### Step 2: Create bin directory

```powershell
if (-not (Test-Path "bin")) { mkdir bin }
```

#### Step 3: Compile

```powershell
javac -cp sqlite-jdbc-3.43.0.0.jar -d bin -sourcepath . `
    Main.java `
    models/User.java `
    models/GameScore.java `
    database/DatabaseManager.java `
    database/UserDAO.java `
    database/GameScoreDAO.java `
    services/AuthenticationService.java `
    gui/LoginPage.java `
    gui/RegisterPage.java `
    gui/Dashboard.java `
    games/TicTacToeGame.java `
    games/SnakeGame.java `
    games/NumberGuessingGame.java
```

#### Step 4: Run

```powershell
cd bin
java -cp ".;../sqlite-jdbc-3.43.0.0.jar" Main
```

---

## Alternative: Using Maven

### Create pom.xml

Create a file named `pom.xml` in the project root:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.gaminghub</groupId>
    <artifactId>gaming-hub</artifactId>
    <version>1.0.0</version>
    <name>Gaming Hub</name>
    <description>A complete gaming hub system with GUI and database</description>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.43.0.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.0.0</version>
                <configuration>
                    <mainClass>Main</mainClass>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### Maven Commands

```bash
# Compile
mvn clean compile

# Run
mvn exec:java -Dexec.mainClass="Main"

# Build JAR
mvn package
```

---

## Verification Checklist

After setup, verify:

- [ ] SQLite JDBC driver is in project root
- [ ] `bin` directory is created
- [ ] All Java files compiled successfully
- [ ] No compilation errors in console
- [ ] Application launches (Login page appears)
- [ ] Database file `gaming_hub.db` is created

---

## Common Issues & Solutions

### Issue: "Command not found: javac"

**Solution:** Install Java Development Kit (JDK)

- macOS: `brew install openjdk`
- Linux: `sudo apt-get install openjdk-11-jdk`
- Windows: Download from oracle.com or use `choco install jdk8`

### Issue: "Cannot find symbol: class org.sqlite"

**Solution:** Ensure SQLite JDBC jar is in the correct location and classpath is set

### Issue: "database is locked"

**Solution:**

- Close all instances of the application
- Delete `gaming_hub.db` file
- Restart the application

### Issue: NullPointerException

**Solution:**

- Replace `gaming_hub.db` with a fresh one
- Ensure DatabaseManager.initialize() is called first

---

## What Happens During First Run

1. **Database Initialization**
   - Checks for `gaming_hub.db`
   - Creates database if it doesn't exist
   - Creates `users` table
   - Creates `game_scores` table

2. **Login Screen**
   - Displays beautiful gradient login interface
   - Click "Register" to create new account

3. **After Registration/Login**
   - Dashboard appears showing user stats
   - Select a game to play
   - Scores are automatically saved to database

---

## File Sizes (Approximate)

```
sqlite-jdbc-3.43.0.0.jar  ~1.5 MB
gaming_hub.db             ~32 KB (after first use)
Compiled .class files     ~2 MB
```

---

## IDE Setup (Optional)

### IntelliJ IDEA

1. Open Project
2. Add SQLite JDBC as external library
3. Run → Edit Configurations
4. Set Main class to `Main`
5. Add VM option: `-cp sqlite-jdbc-3.43.0.0.jar`

### Eclipse

1. Create Java Project
2. Add SQLite JDBC to Build Path
3. Run As → Java Application
4. Select `Main` class

### VS Code

1. Install Java & Spring extensions
2. Open project folder
3. Press F5 to debug
4. Select Java as language

---

## Next Steps

1. ✅ Setup complete - start the application
2. ✅ Register a new account
3. ✅ Play each game at least once
4. ✅ Check your statistics on the dashboard
5. ✅ Explore the code and understand the architecture

**Ready to play? 🎮**
