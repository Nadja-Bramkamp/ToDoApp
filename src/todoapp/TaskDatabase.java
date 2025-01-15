package todoapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TaskDatabase {
    // Use SQLITE as Database
    private static final String DB_URL = "jdbc:sqlite:tasks.db";

    // Save the Task and return the corresponding object that includes the id given by the database
    public Task saveTask(String title, boolean completed) {
        // Create SQL-Statement
        String sql = "INSERT INTO tasks (title, completed) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, title);
            pstmt.setInt(2, completed ? 1 : 0);

            int rowsAffected = pstmt.executeUpdate();

            // Return the inserted Task that contains now the database ID
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        return new Task(id, title);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Hinzufügen der Task: " + e.getMessage());
        }

        return null;
    }

    // Load existing tasks
    public List<Task> loadTasks() {
        // result list
        List<Task> tasks = new ArrayList<>();
        // SQL Statement
        String sql = "SELECT id, title, completed FROM tasks";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Create result list
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                boolean isCompleted = rs.getInt("completed") == 1;

                tasks.add(new Task(id, title, isCompleted)); // Task mit ID hinzufügen
                tasks.get(tasks.size() - 1).setIsCompleted(isCompleted);
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Tasks: " + e.getMessage());
        }

        return tasks;
    }

    // Delete task from database by ID
    public void deleteTask(int id) {
        // SQL Statement
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id); // ID setzen
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Task mit ID " + id + " wurde erfolgreich gelöscht.");
            } else {
                System.out.println("Kein Task mit ID " + id + " gefunden.");
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Löschen des Tasks: " + e.getMessage());
        }
    }

    // Update the completed-Boolean by Database ID
    public void updateTaskStatus(int id, boolean isCompleted) {
        // SQL-Statement
        String sql = "UPDATE tasks SET completed = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set Parameter
            pstmt.setInt(1, isCompleted ? 1 : 0); // 1 = true, 0 = false
            pstmt.setInt(2, id);

            // Execute Statement
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Status von Task mit ID " + id + " wurde erfolgreich aktualisiert.");
            } else {
                System.out.println("Kein Task mit ID " + id + " gefunden.");
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Aktualisieren des Status: " + e.getMessage());
        }
    }


}
