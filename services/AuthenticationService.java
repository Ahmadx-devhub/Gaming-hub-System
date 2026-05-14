package services;

import models.User;
import database.UserDAO;

public class AuthenticationService {
    private static User currentUser;

    public static boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        String u = username.trim();
        if (u.isEmpty() || password.isEmpty()) {
            return false;
        }
        User user = UserDAO.getUserByUsername(u);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public static boolean register(String username, String password, String confirmPassword) {
        if (username == null || password == null || confirmPassword == null) {
            return false;
        }
        String u = username.trim();
        if (u.isEmpty()) {
            return false;
        }
        if (!password.equals(confirmPassword)) {
            return false;
        }
        if (u.length() < 3 || password.length() < 4) {
            return false;
        }
        if (UserDAO.userExists(u)) {
            return false;
        }
        return UserDAO.createUser(u, password);
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
