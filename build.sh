#!/bin/bash

# Gaming Hub - Compilation and Run Script

echo "================================"
echo "Gaming Hub - Build & Run Script"
echo "================================"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if SQLite JDBC driver exists
if [ ! -f "sqlite-jdbc-3.43.0.0.jar" ]; then
    echo -e "${YELLOW}Downloading SQLite JDBC driver...${NC}"
    curl -o sqlite-jdbc-3.43.0.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar
    if [ $? -ne 0 ]; then
        echo -e "${RED}Failed to download SQLite JDBC driver${NC}"
        echo "Please download manually from: https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar"
        exit 1
    fi
fi

# Create bin directory if it doesn't exist
if [ ! -d "bin" ]; then
    echo -e "${YELLOW}Creating bin directory...${NC}"
    mkdir bin
fi

# Compile
echo -e "${YELLOW}Compiling Java files...${NC}"
javac -cp sqlite-jdbc-3.43.0.0.jar -d bin $(find . -name "*.java")

if [ $? -eq 0 ]; then
    echo -e "${GREEN}Compilation successful!${NC}"
    echo ""
    echo -e "${YELLOW}Starting Gaming Hub...${NC}"
    echo ""
    
    # Run
    cd bin
    java -cp .:../sqlite-jdbc-3.43.0.0.jar Main
else
    echo -e "${RED}Compilation failed!${NC}"
    exit 1
fi
