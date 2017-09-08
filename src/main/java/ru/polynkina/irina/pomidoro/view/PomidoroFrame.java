package ru.polynkina.irina.pomidoro.view;

import ru.polynkina.irina.pomidoro.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class PomidoroFrame extends JFrame {

    private static final int AMOUNT_BUTTONS = 5;
    private static final int AMOUNT_COLUMNS = 1;

    private Controller controller;

    private int widthFrame;
    private int heightFrame;

    private JPanel buttonPanel;
    private JTable taskTable;
    private TaskTable table;


    public PomidoroFrame(Controller controller) {
        super("Pomidoro Manager");
        this.controller = controller;
        initializeSizeFrame();
        createButtonPanel();
        createTaskTable();
        createView();
    }

    private void initializeSizeFrame() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        widthFrame = size.width / 2;
        heightFrame = size.height / 2;
        setSize(widthFrame, heightFrame );
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createButtonPanel() {
        JButton addTask = new JButton("Добавить задачу");
        addTask.addActionListener(e -> {
            DialogForCreatingTask dialogFrame = new DialogForCreatingTask(PomidoroFrame.this,"Добавление задачи", controller);
            dialogFrame.setVisible(true);
            while(dialogFrame.isVisible()) {}
            if(dialogFrame.userActionsIsSuccessful()) updateTaskTable();
        });

        JButton editTask = new JButton("Редактировать задачу");
        editTask.addActionListener(e -> {
        });

        JButton closeTask = new JButton("Закрыть задачу");
        closeTask.addActionListener(e -> {
        });

        JButton deleteTask = new JButton("Удалить задачу");
        deleteTask.addActionListener(e -> {
        });

        JButton startWork = new JButton("Начать работу над задачей");
        startWork.addActionListener(e -> {
        });

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(AMOUNT_BUTTONS, AMOUNT_COLUMNS));
        buttonPanel.add(addTask);
        buttonPanel.add(editTask);
        buttonPanel.add(closeTask);
        buttonPanel.add(deleteTask);
        buttonPanel.add(startWork);
    }

    private void createTaskTable() {
        table = new TaskTable(controller.selectActiveTask());
        taskTable = new JTable(table);
        taskTable.setRowHeight(heightFrame / 15);
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(widthFrame / 2);
    }

    private void updateTaskTable() {
        table.insertTask(controller.selectLastTask());
        taskTable.repaint();
        taskTable.revalidate();
    }

    private void createView() {
        setLayout(new BorderLayout());
        JPanel flowLayoutButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        flowLayoutButtonPanel.add(buttonPanel);
        add(flowLayoutButtonPanel, BorderLayout.EAST);
        add(new JScrollPane(taskTable), BorderLayout.CENTER);
    }
}
