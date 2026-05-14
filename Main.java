import database.DatabaseManager;
import gui.GameHubTheme;
import gui.LoginPage;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initialize();
        GameHubTheme.installGlobalUiHints();

        javax.swing.SwingUtilities.invokeLater(() -> new LoginPage());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseManager.closeConnection();
        }));
    }
}
