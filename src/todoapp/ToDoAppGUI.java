package todoapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;

public class ToDoAppGUI {
    private TaskManager taskManager;
    private JFrame frame; //
    private JTextField taskField; // Insert Task
    private JPanel taskDisplayPanel; // Display tasks
    private JButton addButton, deleteButton;

    public ToDoAppGUI() {
        taskManager = new TaskManager();
        frame = new JFrame("ToDo App");
        taskField = new JTextField(20);
        taskDisplayPanel = new JPanel();
        taskDisplayPanel.setLayout(new BoxLayout(taskDisplayPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(taskDisplayPanel);

        // Define Font
        Font font = new Font("Arial", Font.PLAIN, 20);

        // Define Buttons
        addButton = new JButton("Aufgabe hinzufügen");
        addButton.setFont(font);
        addButton.setBackground(new Color(125, 175, 255));

        deleteButton = new JButton("Aufgabe löschen");
        deleteButton.setFont(font);
        deleteButton.setBackground(new Color(125, 175, 255));

        // Define Layouts
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        taskField.setFont(font);
        taskField.setMaximumSize(new Dimension(Integer.MAX_VALUE, taskField.getPreferredSize().height));

        taskDisplayPanel.setFont(font);

        // Add components
        frame.add(taskField);
        frame.add(addButton);
        frame.add(deleteButton);
        frame.add(scrollPane);

        // ActionListener
        addButton.addActionListener(new AddTaskListener());
        deleteButton.addActionListener(new DeleteActionListener());

        // Window setting
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Display the existing tasks
        updateTaskDisplay();
    }

    // Display the tasks after adding a new one
    class AddTaskListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String taskTitle = taskField.getText();
            if (!taskTitle.isEmpty()) {
                taskManager.addTask(taskTitle);
                taskField.setText("");
                updateTaskDisplay();
            }
        }
    }

    // Open Dialog to get the index of the task, display the tasks after deleting the task
    class DeleteActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String indexText = JOptionPane.showInputDialog(frame, "Index der zu löschenden Aufgabe");
            try {
                int index = Integer.parseInt(indexText);
                taskManager.deleteTask(index - 1);
                updateTaskDisplay();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Ungültiger Index");
            }
        }
    }

    // Display the tasks with their index
    private void updateTaskDisplay() {
        taskDisplayPanel.removeAll();
        int index = 1;

        // Add all tasks to the display
        for (Task task : taskManager.getTasks()) {
            JCheckBox checkBox = new JCheckBox("(" + index++ + ") " + task.getTitle(), task.getIsCompleted());
            checkBox.setFont(new Font("Arial", Font.PLAIN, 20));

            // Checkbox can be selected
            checkBox.addActionListener(e -> {
                task.setIsCompleted(checkBox.isSelected());
            });

            // If the state of the checkbox changes, then update the database
            checkBox.addItemListener(e -> {
                boolean isChecked = (e.getStateChange() == ItemEvent.SELECTED);
                task.setIsCompleted(isChecked); // Aktualisiere das Objekt
                this.taskManager.getTaskDatabase().updateTaskStatus(task.getId(), isChecked); // Aktualisiere die Datenbank
            });

            taskDisplayPanel.add(checkBox);
        }

        taskDisplayPanel.revalidate();
        taskDisplayPanel.repaint();
    }


    public static void main(String[] args) {
        new ToDoAppGUI();
    }
}
