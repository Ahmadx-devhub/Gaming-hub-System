# Gaming Hub - Java Project

A complete, functional gaming hub system with user authentication, multiple games, and score tracking.

## Features

✅ **User Authentication**

- Login system with password
- User registration with validation
- Secure password storage

✅ **Beautiful GUI**

- Gradient-styled login and registration pages
- Interactive dashboard with user statistics
- Professional game interfaces

✅ **Three Games**

1. **Tic Tac Toe** - Classic turn-based strategy game
2. **Snake** - Arrow key controlled snake game
3. **Number Guessing** - Guess a random number 1-100 with hints

✅ **Game Statistics**

- Score tracking per game
- Total accumulated score
- Total playtime tracking (hours and minutes)
- Game history stored in database

✅ **Backend Database**

- SQLite database (gaming_hub.db)
- User data storage
- Game scores and statistics
- Automatic database initialization

## Project Structure

```
java_project/
├── Main.java                    # Entry point
├── models/
│   ├── User.java               # User model
│   └── GameScore.java          # Game score model
├── database/
│   ├── DatabaseManager.java    # Database connection and initialization
│   ├── UserDAO.java            # User data access object
│   └── GameScoreDAO.java       # Game score data access object
├── services/
│   └── AuthenticationService.java  # Login/register logic
├── gui/
│   ├── LoginPage.java          # Login interface
│   ├── RegisterPage.java       # Registration interface
│   └── Dashboard.java          # Main dashboard
├── games/
│   ├── TicTacToeGame.java      # Tic Tac Toe game
│   ├── SnakeGame.java          # Snake game
│   └── NumberGuessingGame.java # Number guessing game
└── README.md                    # This file
```

## Requirements

- Java 8 or higher
- SQLite JDBC driver (included in compile instructions)

## How to Compile

### Option 1: Using javac (One-line compilation)

First, download SQLite JDBC driver:

```bash
cd /Users/apple/Desktop/projects/java_project
wget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar
```

Then compile:

```bash
javac -cp sqlite-jdbc-3.43.0.0.jar -d bin $(find . -name "*.java")
```

### Option 2: Using Maven

Create a `pom.xml` in the project root:

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.gaminghub</groupId>
    <artifactId>gaming-hub</artifactId>
    <version>1.0</version>

    <dependencies>
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.43.0.0</version>
        </dependency>
    </dependencies>
</project>
```

Then compile with Maven:

```bash
mvn clean compile
```

## How to Run

### Option 1: After manual compilation

```bash
java -cp .:sqlite-jdbc-3.43.0.0.jar Main
```

### Option 2: Using Maven

```bash
mvn exec:java -Dexec.mainClass="Main"
```

## How to Use

1. **Start the Application** - Run the Main class
2. **Login/Register** - Enter credentials or create a new account
3. **Dashboard** - View your stats and select a game
4. **Play Games** - Choose from Tic Tac Toe, Snake, or Number Guessing
5. **Score Tracking** - Your scores and playtime are automatically saved

## Game Rules

### Tic Tac Toe

- X and O take turns clicking empty squares
- Win: Get 3 in a row, column, or diagonal → +10 points
- Draw: Board full with no winner → +5 points
- Play as many rounds as you want

### Snake

- Use arrow keys to move the snake
- Eat red food to grow and earn points
- Don't hit the walls or yourself
- Each food eaten → +10 points
- Press ENTER to return after game over

### Number Guessing

- Guess a number between 1 and 100
- Get hints: "Too high" or "Too low"
- Maximum 10 attempts
- Score = 100 - (attempts × 10)
- Negative scores become 0

## Database

The system uses SQLite with automatic database initialization:

- **Users table** - Stores username, password, total score, total play time
- **Game scores table** - Stores individual game results with timestamps

Database file: `gaming_hub.db` (created automatically on first run)

## Default Login Credentials

After registration, use your own credentials:

- Username: Your chosen username (3+ characters)
- Password: Your chosen password (4+ characters)

## Features Implemented

- ✅ User Authentication (Login/Register)
- ✅ Beautiful GUI with gradients
- ✅ Three fully functional games
- ✅ Score and playtime tracking
- ✅ Database persistence
- ✅ User statistics dashboard
- ✅ Game history storage
- ✅ Secure password handling (in production, use hashing)
- ✅ Multi-user support

## Future Enhancements

- Leaderboard system
- Game difficulty levels
- More games (Hangman, Memory Match, etc.)
- Password hashing with bcrypt
- User profile customization
- Achievements and badges
- Multiplayer support

## Troubleshooting

**"Cannot resolve symbol 'gui'"**

- Ensure all files are compiled with the correct classpath

**"Database locked"**

- Close all instances of the application
- Delete `gaming_hub.db` and restart

**"Class not found: org.sqlite.JDBC"**

- Download and add the SQLite JDBC driver to your classpath

## Author Notes

This is a complete, educational gaming hub system demonstrating:

- Java Swing GUI development
- Database design and management
- Object-oriented programming principles
- User authentication systems
- Game development fundamentals

Enjoy your gaming hub! 🎮
