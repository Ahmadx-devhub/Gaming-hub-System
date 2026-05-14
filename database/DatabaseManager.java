package database;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class DatabaseManager {

    private static final String SQLITE_JAR_NAME = "sqlite-jdbc-3.43.0.0.jar";
    private static final String SQLITE_DOWNLOAD =
            "https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/sqlite-jdbc-3.43.0.0.jar";

    private static final String DB_PATH = System.getProperty("user.home") + File.separator + "Gaming-Hub" + File.separator + "gaming_hub.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static Connection connection;
    private static String lastInitError;
    /** Holds SQLite JDBC jar so the driver class stays loaded for DriverManager. */
    private static URLClassLoader sqliteJarLoader;

    public static void initialize() {
        lastInitError = null;
        connection = null;
        try {
            File dbDir = new File(System.getProperty("user.home") + File.separator + "Gaming-Hub");
            if (!dbDir.exists() && !dbDir.mkdirs()) {
                lastInitError = "Could not create directory: " + dbDir.getAbsolutePath();
                System.err.println(lastInitError);
                return;
            }

            if (!ensureSqliteDriverLoaded(dbDir)) {
                return;
            }

            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true);
            createTables();
        } catch (SQLException e) {
            lastInitError = "Database connection error: " + e.getMessage();
            System.err.println(lastInitError);
            e.printStackTrace();
            connection = null;
        }
    }

    /**
     * Loads SQLite JDBC from the classpath, or from a jar on disk next to the project,
     * or downloads it into ~/Gaming-Hub/ so runs from an IDE (without -cp) still work.
     */
    private static boolean ensureSqliteDriverLoaded(File dbDir) {
        try {
            Class.forName("org.sqlite.JDBC");
            return true;
        } catch (ClassNotFoundException ignored) {
            // load from disk or download
        }

        File jar = findSqliteJarOnDisk();
        if (jar == null || !jar.isFile()) {
            File fallback = new File(dbDir, SQLITE_JAR_NAME);
            try {
                System.out.println("SQLite driver not on classpath — downloading to " + fallback.getAbsolutePath());
                downloadSqliteJar(fallback);
                jar = fallback;
            } catch (IOException e) {
                lastInitError = "SQLite JDBC missing and download failed: " + e.getMessage()
                        + ". Add " + SQLITE_JAR_NAME + " to the classpath or place it in " + dbDir.getAbsolutePath();
                System.err.println(lastInitError);
                e.printStackTrace();
                return false;
            }
        }

        try {
            loadDriverFromJar(jar);
            return true;
        } catch (Exception e) {
            lastInitError = "Could not load SQLite JDBC from " + jar.getAbsolutePath() + ": " + e.getMessage();
            System.err.println(lastInitError);
            e.printStackTrace();
            return false;
        }
    }

    private static File findSqliteJarOnDisk() {
        try {
            URL loc = DatabaseManager.class.getProtectionDomain().getCodeSource().getLocation();
            File start = new File(loc.toURI());
            if (start.isFile()) {
                start = start.getParentFile();
            }
            for (File d = start; d != null; d = d.getParentFile()) {
                File j = new File(d, SQLITE_JAR_NAME);
                if (j.isFile()) {
                    return j;
                }
            }
        } catch (Exception e) {
            System.err.println("Driver jar search (from class): " + e.getMessage());
        }

        File cwd = new File(System.getProperty("user.dir"));
        File[] direct = {
                new File(cwd, SQLITE_JAR_NAME),
                new File(cwd, "Gaming-hub-System" + File.separator + SQLITE_JAR_NAME),
        };
        for (File f : direct) {
            if (f.isFile()) {
                return f;
            }
        }
        for (File d = cwd; d != null; d = d.getParentFile()) {
            File nested = new File(d, "Gaming-hub-System" + File.separator + SQLITE_JAR_NAME);
            if (nested.isFile()) {
                return nested;
            }
        }
        return null;
    }

    private static void downloadSqliteJar(File target) throws IOException {
        File parent = target.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Cannot create directory: " + parent);
        }

        URI uri = URI.create(SQLITE_DOWNLOAD);
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setConnectTimeout(20000);
        conn.setReadTimeout(120000);
        conn.setInstanceFollowRedirects(true);
        int code = conn.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            conn.disconnect();
            throw new IOException("HTTP " + code + " from " + SQLITE_DOWNLOAD);
        }
        try (InputStream in = new BufferedInputStream(conn.getInputStream());
             OutputStream out = new BufferedOutputStream(new FileOutputStream(target))) {
            byte[] buf = new byte[16384];
            int n;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
            }
        } finally {
            conn.disconnect();
        }
    }

    private static void loadDriverFromJar(File jar) throws Exception {
        if (sqliteJarLoader != null) {
            return;
        }
        URL jarUrl = jar.toURI().toURL();
        ClassLoader parent = classLoaderParentForJdbc();
        sqliteJarLoader = new URLClassLoader(new URL[]{jarUrl}, parent);
        Class<?> jdbcClass = Class.forName("org.sqlite.JDBC", true, sqliteJarLoader);
        Driver real = (Driver) jdbcClass.getDeclaredConstructor().newInstance();
        DriverManager.registerDriver(new ForwardingDriver(real));
    }

    private static ClassLoader classLoaderParentForJdbc() {
        try {
            return (ClassLoader) ClassLoader.class.getMethod("getPlatformClassLoader").invoke(null);
        } catch (Throwable ignored) {
            return ClassLoader.getSystemClassLoader();
        }
    }

    private static void createTables() {
        if (connection == null) {
            return;
        }
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "total_play_time INTEGER DEFAULT 0," +
                    "total_score INTEGER DEFAULT 0," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            stmt.execute("CREATE TABLE IF NOT EXISTS game_scores (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "game_name TEXT NOT NULL," +
                    "score INTEGER NOT NULL," +
                    "play_time INTEGER NOT NULL," +
                    "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE)");
        } catch (SQLException e) {
            System.err.println("Table creation error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isReady() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static String getLastInitError() {
        return lastInitError;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }

    /** Bridges DriverManager (system classloader) to a driver loaded from a URLClassLoader. */
    private static final class ForwardingDriver implements Driver {
        private final Driver delegate;

        ForwardingDriver(Driver delegate) {
            this.delegate = delegate;
        }

        @Override
        public Connection connect(String url, Properties info) throws SQLException {
            return delegate.connect(url, info);
        }

        @Override
        public boolean acceptsURL(String url) throws SQLException {
            return delegate.acceptsURL(url);
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
            return delegate.getPropertyInfo(url, info);
        }

        @Override
        public int getMajorVersion() {
            return delegate.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return delegate.getMinorVersion();
        }

        @Override
        public boolean jdbcCompliant() {
            return delegate.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return delegate.getParentLogger();
        }
    }
}
