import database.DatabaseManager;
import gui.LoginPage;

public class Main {
    public static void main(String[] args) {
        // Initialize database
        DatabaseManager.initialize();
        
        // Start the application with login page
        new LoginPage();
        
        // Cleanup on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseManager.closeConnection();
        }));
    }
}