import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Prerequisites:
 *   1. Download SQLite JDBC driver: https://github.com/xerial/sqlite-jdbc/releases
 *   2. Place sqlite-jdbc-<version>.jar in the ex31/ directory.
 *   3. Compile: javac -cp .:sqlite-jdbc-*.jar BasicJDBCConnection.java
 *   4. Run    : java  -cp .:sqlite-jdbc-*.jar BasicJDBCConnection
 */
public class BasicJDBCConnection {

    private static final String DB_URL = "jdbc:sqlite:school.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Create table if it does not exist
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS students (
                        id   INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT    NOT NULL,
                        age  INTEGER NOT NULL
                    )
                    """);

            // Seed some data only when the table is empty
            ResultSet check = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM students");
            if (check.getInt("cnt") == 0) {
                stmt.execute("INSERT INTO students (name, age) VALUES ('Alice', 20)");
                stmt.execute("INSERT INTO students (name, age) VALUES ('Bob', 22)");
                stmt.execute("INSERT INTO students (name, age) VALUES ('Charlie', 19)");
                System.out.println("Sample data inserted.");
            }

            // Query and display
            System.out.println("\nStudents table:");
            System.out.printf("%-5s %-15s %-5s%n", "ID", "Name", "Age");
            System.out.println("-".repeat(27));
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                System.out.printf("%-5d %-15s %-5d%n",
                        rs.getInt("id"), rs.getString("name"), rs.getInt("age"));
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
