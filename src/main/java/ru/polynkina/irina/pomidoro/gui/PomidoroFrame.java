package ru.polynkina.irina.pomidoro.gui;

import javax.swing.*;
import java.awt.*;

public class PomidoroFrame extends JFrame {

    private static int widthFrame;
    private static int heightFrame;

    private static JPanel buttonLayout;
    private static JButton addTask;
    private static JButton editTask;
    private static JButton deleteTask;

    private static JPanel taskLayout;
    private static JTable tasks;
    private static Object[][] data = new String[][]{
            {"1", "Task1", "A", "description1"},
            {"2", "Task2", "B", "description2"},
            {"1", "Task1", "A", "description1"},
            {"2", "Task2", "B", "description2"},
            {"3", "Task3", "C", "description3"},
            {"4", "Task4", "C", "description4"},
            {"3", "Task3", "C", "description3"},
            {"4", "Task4", "C", "description4"},
            {"5", "Task5", "C", "description5"}
    };

    private static Object[] headers = {"id", "Task", "Priority" , "Deadline"};

    public PomidoroFrame() {
        super("Pomidoro Manager");
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        widthFrame = size.width / 2;
        heightFrame = size.height / 2;
        setSize(widthFrame, heightFrame );
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        buttonLayout = new JPanel();
        buttonLayout.setLayout(new GridLayout(3, 1));
        addTask = new JButton("Добавить задачу");
        editTask = new JButton("Редактировать задачу");
        deleteTask = new JButton("Удалить задачу");
        buttonLayout.add(addTask);
        buttonLayout.add(editTask);
        buttonLayout.add(deleteTask);
        add(buttonLayout);

        taskLayout = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tasks = new JTable(data, headers);
        tasks.setFillsViewportHeight(true);
        tasks.setRowHeight(heightFrame / 15);


        taskLayout.add(tasks);
        taskLayout.add(buttonLayout);
        taskLayout.setBackground(Color.CYAN);

        add(taskLayout);
        pack();
    }
}
