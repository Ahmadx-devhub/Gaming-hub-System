# Quick Start Guide

## Easy Setup & Run (3 Steps)

### Step 1: Download SQLite Driver

Go to your project directory and download the SQLite JDBC driver:

```bash
cd /Users/apple/Desktop/projects/java_project
wget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar
```

Or manually download from:
https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar

### Step 2: Compile the Project

Make sure you're in the project directory:

```bash
cd /Users/apple/Desktop/projects/java_project
```

**On Mac/Linux:**

```bash
chmod +x build.sh
./build.sh
```

**On Windows:**

```batch
build.bat
```

**Manual Compilation (All platforms):**

```bash
mkdir -p bin
javac -cp sqlite-jdbc-3.43.0.0.jar -d bin $(find . -name "*.java")
```

### Step 3: Run the Application

**On Mac/Linux (using build script):**

```bash
./build.sh
```

**Manual Run:**

```bash
cd bin
java -cp .:../sqlite-jdbc-3.43.0.0.jar Main
cd ..
```

**On Windows (using build script):**

```batch
build.bat
```

**Manual Run (Windows):**

```batch
cd bin
java -cp .;..\sqlite-jdbc-3.43.0.0.jar Main
cd ..
```

---

## First Time Use

1. Click **"Register"** to create a new account
   - Username: 3+ characters
   - Password: 4+ characters
   - Confirm password must match

2. Login with your new credentials

3. Select a game from the dashboard

4. Enjoy! Your scores are automatically saved

---

## Game Controls

**Tic Tac Toe:** Click any empty square

**Snake:** Use arrow keys (↑ ↓ ← →)

**Number Guessing:** Type a number and press ENTER

---

## Troubleshooting

**Problem:** "Cannot resolve symbol"

- **Solution:** Make sure you've downloaded the SQLite JDBC driver and it's in the project directory

**Problem:** "Class not found: org.sqlite.JDBC"

- **Solution:** Add sqlite-jdbc-3.43.0.0.jar to the classpath when compiling and running

**Problem:** Database errors

- **Solution:** Delete `gaming_hub.db` file and restart the application

**Problem:** Compilation errors on Windows

- **Solution:** Make sure to use the correct path separators (\ for classpath separator)

---

## Project Files Overview

- **Main.java** - Starts the application
- **gui/** - All graphical interfaces
- **games/** - All game logic
- **database/** - Database management
- **models/** - Data models
- **services/** - Business logic

---

## Features

✅ User Login & Registration
✅ Beautiful GUI Interface
✅ 3 Fun Games: Tic Tac Toe, Snake, Number Guessing
✅ Score Tracking
✅ Play Time Tracking
✅ Database Storage
✅ Multi-user Support

---

## Need Help?

- Check README.md for detailed information
- Review the project structure
- Check console output for error messages
- Delete gaming_hub.db if database issues occur

Enjoy your Gaming Hub! 🎮
