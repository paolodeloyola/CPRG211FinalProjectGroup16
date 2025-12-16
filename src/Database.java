import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DB_FILE = "database.db";

    public static Connection getConnection() throws SQLException {
        Path dbPath = Paths.get(DB_FILE).toAbsolutePath();
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
            st.execute("PRAGMA busy_timeout = 5000;");
        }
        return conn;
    }

    public static void init() throws SQLException {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            st.execute("""
            CREATE TABLE IF NOT EXISTS Department (
              department_id INTEGER PRIMARY KEY AUTOINCREMENT,
              name TEXT NOT NULL UNIQUE
            );
            """);

            st.execute("""
            CREATE TABLE IF NOT EXISTS Employee (
              employee_id INTEGER PRIMARY KEY AUTOINCREMENT,
              public_id INTEGER UNIQUE,
              full_name TEXT NOT NULL,
              email TEXT NOT NULL UNIQUE,
              job_title TEXT NOT NULL,
              department_id INTEGER,
              active INTEGER NOT NULL DEFAULT 1,
              FOREIGN KEY (department_id) REFERENCES Department(department_id)
            );
            """);
        }
    }
}
