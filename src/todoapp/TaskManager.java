package todoapp;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;
    private TaskDatabase taskDatabase;

    public TaskManager() {
        this.taskDatabase = new TaskDatabase();
        this.tasks = taskDatabase.loadTasks();
    }

    public TaskDatabase getTaskDatabase() {
        return this.taskDatabase;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    // Create the new task and store it with DatabaseID
    public void addTask(String title) {
        Task task = new Task(title);
        Task taskWithID = this.taskDatabase.saveTask(task.getTitle(), task.getIsCompleted());
        this.tasks.add(taskWithID);
    }

    // Delete the task by using the index and the database ID
    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            Task task = this.tasks.get(index);
            this.tasks.remove(index);
            this.taskDatabase.deleteTask(task.getId());
        } else {
            System.out.println("Invalid index");
        }

    }
}
