import ToDoApp.Task;
import ToDoApp.TaskDatabase;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskDatabaseTest {
    private static TaskDatabase taskDatabase;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        taskDatabase = new TaskDatabase(DriverManager.getConnection("jdbc:sqlite:test_tasks.db"));

        // Create table
        String createTableSQL = "CREATE TABLE test_tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "completed BOOLEAN NOT NULL DEFAULT 0)";
        try (Statement stmt = taskDatabase.getConnection().createStatement()) {
            stmt.executeUpdate(createTableSQL);
        }
    }

    @AfterAll
    static void teardownDatabase() throws SQLException {
        taskDatabase.getConnection().close();
    }

    @BeforeEach
    void clearDatabase() throws SQLException {
        // Delete all data before each test
        try (Statement stmt = taskDatabase.getConnection().createStatement()) {
            stmt.execute("DELETE FROM tasks");
        }
    }

    @Test
    void testAddTask() {
        taskDatabase.saveTask("Test Task", false);

        List<Task> tasks = taskDatabase.loadTasks();
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        assertFalse(tasks.get(0).getIsCompleted());

    }
}
