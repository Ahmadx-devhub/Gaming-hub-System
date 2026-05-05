package services;

import models.User;
import database.UserDAO;

public class AuthenticationService {
    private static User currentUser;

    public static boolean login(String username, String password) {
        User user = UserDAO.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public static boolean register(String username, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return false;
        }

        if (username.length() < 3 || password.length() < 4) {
            return false;
        }

        if (UserDAO.userExists(username)) {
            return false;
        }

        return UserDAO.createUser(username, password);
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
