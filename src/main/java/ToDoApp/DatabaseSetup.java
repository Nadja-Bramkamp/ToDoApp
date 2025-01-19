package ToDoApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseSetup {
    private static final String DB_URL = "jdbc:sqlite:tasks.db";

    // Create tasks Table if it doesn not exists
    public static void createTasksTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                completed BOOLEAN NOT NULL
            );
            """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabelle 'tasks' erfolgreich erstellt (oder existiert bereits).");
        } catch (Exception e) {
            System.err.println("Fehler beim Erstellen der Tabelle: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createTasksTable();
    }
}

