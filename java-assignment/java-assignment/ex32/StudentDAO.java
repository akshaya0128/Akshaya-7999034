import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Prerequisites: same as ex31 — SQLite JDBC driver on the classpath.
 * Compile: javac -cp .:sqlite-jdbc-*.jar StudentDAO.java
 * Run    : java  -cp .:sqlite-jdbc-*.jar StudentDAO
 */
public class StudentDAO {

    private static final String DB_URL = "jdbc:sqlite:school.db";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS students (
                    id   INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT    NOT NULL,
                    age  INTEGER NOT NULL
                )
                """;
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public void insert(String name, int age) {
        String sql = "INSERT INTO students (name, age) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            int rows = pstmt.executeUpdate();
            System.out.println("Inserted " + rows + " row(s): " + name + ", age " + age);
        } catch (SQLException e) {
            System.out.println("Insert error: " + e.getMessage());
        }
    }

    public void update(int id, String newName, int newAge) {
        String sql = "UPDATE students SET name = ?, age = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newAge);
            pstmt.setInt(3, id);
            int rows = pstmt.executeUpdate();
            System.out.println("Updated " + rows + " row(s) for ID " + id);
        } catch (SQLException e) {
            System.out.println("Update error: " + e.getMessage());
        }
    }

    public void displayAll() {
        String sql = "SELECT * FROM students";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.printf("%-5s %-15s %-5s%n", "ID", "Name", "Age");
            System.out.println("-".repeat(27));
            while (rs.next()) {
                System.out.printf("%-5d %-15s %-5d%n",
                        rs.getInt("id"), rs.getString("name"), rs.getInt("age"));
            }
        } catch (SQLException e) {
            System.out.println("Query error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();
        dao.createTable();

        dao.insert("Alice", 20);
        dao.insert("Bob", 22);

        System.out.println("\nBefore update:");
        dao.displayAll();

        dao.update(1, "Alice Smith", 21);

        System.out.println("\nAfter update:");
        dao.displayAll();
    }
}
