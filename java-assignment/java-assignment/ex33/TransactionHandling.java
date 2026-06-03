import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Prerequisites: SQLite JDBC driver on the classpath (same as ex31).
 * Compile: javac -cp .:sqlite-jdbc-*.jar TransactionHandling.java
 * Run    : java  -cp .:sqlite-jdbc-*.jar TransactionHandling
 */
public class TransactionHandling {

    private static final String DB_URL = "jdbc:sqlite:bank.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            setup(conn);
            printBalances(conn, "Initial balances");

            // Successful transfer: Alice sends 200 to Bob
            transfer(conn, 1, 2, 200.0);
            printBalances(conn, "After transferring 200 from Alice to Bob");

            // Failed transfer: overdraft attempt (Alice only has 300 left)
            transfer(conn, 1, 2, 5000.0);
            printBalances(conn, "After failed overdraft attempt");

        } catch (SQLException e) {
            System.out.println("Fatal DB error: " + e.getMessage());
        }
    }

    static void setup(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS accounts (
                        id      INTEGER PRIMARY KEY,
                        name    TEXT    NOT NULL,
                        balance REAL    NOT NULL
                    )
                    """);
            stmt.execute("DELETE FROM accounts");
            stmt.execute("INSERT INTO accounts VALUES (1, 'Alice', 500.0)");
            stmt.execute("INSERT INTO accounts VALUES (2, 'Bob',   300.0)");
        }
    }

    static void transfer(Connection conn, int fromId, int toId, double amount) {
        String debit  = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";
        String credit = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement debitStmt = conn.prepareStatement(debit);
                 PreparedStatement creditStmt = conn.prepareStatement(credit)) {

                debitStmt.setDouble(1, amount);
                debitStmt.setInt(2, fromId);
                debitStmt.setDouble(3, amount);
                int debitRows = debitStmt.executeUpdate();

                if (debitRows == 0) {
                    throw new SQLException("Insufficient funds for account ID " + fromId);
                }

                creditStmt.setDouble(1, amount);
                creditStmt.setInt(2, toId);
                creditStmt.executeUpdate();

                conn.commit();
                System.out.printf("Transfer of %.2f succeeded.%n", amount);

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transfer rolled back: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Transaction error: " + e.getMessage());
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    static void printBalances(Connection conn, String label) throws SQLException {
        System.out.println("\n" + label + ":");
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, balance FROM accounts")) {
            while (rs.next()) {
                System.out.printf("  %-10s $%.2f%n", rs.getString("name"), rs.getDouble("balance"));
            }
        }
    }
}
