package ru.polynkina.irina.pomidoro.view;

import ru.polynkina.irina.pomidoro.PomidoroTimer;
import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.Task;
import ru.polynkina.irina.pomidoro.view.addeditdialogs.AddTaskDialog;
import ru.polynkina.irina.pomidoro.view.addeditdialogs.EditTaskDialog;
import ru.polynkina.irina.pomidoro.view.okcanceldialogs.CloseTaskDialog;
import ru.polynkina.irina.pomidoro.view.okcanceldialogs.DeleteTaskDialog;
import ru.polynkina.irina.pomidoro.view.okcanceldialogs.EndWorkDialog;
import ru.polynkina.irina.pomidoro.view.okcanceldialogs.StartWorkDialog;
import ru.polynkina.irina.pomidoro.view.tasktable.TaskTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalTime;
import java.util.Properties;

public class PomidoroFrame extends JFrame {

    private static final int AMOUNT_BUTTONS = 9;
    private static final int AMOUNT_COLUMNS = 1;

    private Controller controller;
    private Task taskForWork;

    private int widthFrame;
    private int heightFrame;

    private JPanel buttonPanel;
    private JTable taskTable;
    private TaskTable table;

    private PomidoroTimer pomidoroTimer;
    private  Timer timer;
    private JTextField timeField;


    public PomidoroFrame(Controller controller) {
        super("Pomidoro Manager");
        this.controller = controller;
        initializeSizeFrame();
        createButtonPanel();
        createTaskTable();
        createView();

        pomidoroTimer = new PomidoroTimer();
        timer = new Timer(0, event -> {
            pomidoroTimer.run();
            if(pomidoroTimer.isTimeWork()) {
                timeField.setText("Время работы: " + LocalTime.ofSecondOfDay(pomidoroTimer.getTimeWorkUntilEnd()).toString());
            } else {
                timeField.setText("Время отдыха: " + LocalTime.ofSecondOfDay(pomidoroTimer.getTimePauseUntilEnd()).toString());
            }
        });
    }

    private void initializeSizeFrame() {
        String homeDir = System.getProperty("user.home");
        Properties properties = new Properties();
        String settingsFilename = homeDir + File.separator + "pomidoro.properties";
        try {
            FileInputStream input = new FileInputStream(settingsFilename);
            properties.load(input);
            input.close();
            int xPos = Integer.parseInt(properties.getProperty("xPos"));
            int yPos = Integer.parseInt(properties.getProperty("yPos"));
            widthFrame = Integer.parseInt(properties.getProperty("width"));
            heightFrame = Integer.parseInt(properties.getProperty("height"));
            setBounds(xPos, yPos, widthFrame, heightFrame);
            setVisible(true);
        } catch(Exception exc) {
            // pomidoro.properties does not exist.
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            widthFrame = size.width / 2;
            heightFrame = size.height / 2;
            setSize(widthFrame, heightFrame );
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
    }

    private void createButtonPanel() {
        JButton addTask = new JButton(new ImageIcon("src\\main\\resources\\img\\add.png"));
        addTask.setText("Добавить задачу");
        addTask.addActionListener(e -> {
            AddTaskDialog dialogFrame = new AddTaskDialog(PomidoroFrame.this,"Добавление задачи", controller);
            dialogFrame.setVisible(true);
            if(dialogFrame.userActionsIsSuccessful()) updateTaskTable();
        });

        JButton editTask = new JButton(new ImageIcon("src\\main\\resources\\img\\edit.png"));
        editTask.setText("Редактировать задачу");
        editTask.addActionListener(e -> {
            EditTaskDialog dialog = new EditTaskDialog(PomidoroFrame.this,
                    "Редактирование задачи", controller, table.getTask(taskTable.getSelectedRow()));
            dialog.setVisible(true);
            if(dialog.userActionsIsSuccessful()) updateTaskTable();
        });

        JButton closeTask = new JButton(new ImageIcon("src\\main\\resources\\img\\close.png"));
        closeTask.setText("Закрыть задачу");
        closeTask.addActionListener(e -> {
            CloseTaskDialog dialog = new CloseTaskDialog(PomidoroFrame.this,
                    "Закрытие задачи", controller, table.getTask(taskTable.getSelectedRow()));
            dialog.setVisible(true);
            if(dialog.userActionsIsSuccessful()) updateTaskTable();
        });

        JButton deleteTask = new JButton(new ImageIcon("src\\main\\resources\\img\\delete.png"));
        deleteTask.setText("Удалить задачу");
        deleteTask.addActionListener(e -> {
            DeleteTaskDialog dialog = new DeleteTaskDialog(PomidoroFrame.this,
                    "Удаление задачи", controller, table.getTask(taskTable.getSelectedRow()));
            dialog.setVisible(true);
            if(dialog.userActionsIsSuccessful()) updateTaskTable();
        });

        JButton showAutoTask = new JButton(new ImageIcon("src\\main\\resources\\img\\auto.png"));
        showAutoTask.setText("Авто-задачи");
        showAutoTask.addActionListener(e -> {
            AutoTaskFrame autoTaskFrame = new AutoTaskFrame(PomidoroFrame.this, controller);
            autoTaskFrame.setVisible(true);
        });

        JButton showCloseTask = new JButton(new ImageIcon("src\\main\\resources\\img\\closeTask.png"));
        showCloseTask.setText("Закрытые задачи");
        showCloseTask.addActionListener(e -> {
            CloseTaskFrame closeTaskFrame = new CloseTaskFrame(PomidoroFrame.this, controller);
            closeTaskFrame.setVisible(true);
        });

        timeField = new JTextField();
        timeField.setEnabled(false);
        timeField.setDisabledTextColor(Color.RED);

        JButton startWork = new JButton(new ImageIcon("src\\main\\resources\\img\\work.png"));
        startWork.setText("Работать над задачей");
        startWork.addActionListener((ActionEvent e) -> {
            new Thread(() -> {
                taskForWork = table.getTask(taskTable.getSelectedRow());
                if(taskForWork != null) {
                    StartWorkDialog dialog = new StartWorkDialog(PomidoroFrame.this, "Начать работу",
                            controller, taskForWork, timer);
                    dialog.setVisible(true);
                    if(dialog.userActionsIsSuccessful()) {
                        addTask.setEnabled(false);
                        editTask.setEnabled(false);
                        closeTask.setEnabled(false);
                        deleteTask.setEnabled(false);
                        startWork.setEnabled(false);
                        showAutoTask.setEnabled(false);
                        showCloseTask.setEnabled(false);
                    }
                }
            }).start();
        });

        JButton endWork = new JButton(new ImageIcon("src\\main\\resources\\img\\stop.png"));
        endWork.setText("Остановить работу");
        endWork.addActionListener(e -> {
            EndWorkDialog dialog = new EndWorkDialog(PomidoroFrame.this, "Завершить работу",
                    controller, taskForWork, pomidoroTimer, timer);
            dialog.setVisible(true);
            if(dialog.userActionsIsSuccessful()) {
                updateTaskTable();
                addTask.setEnabled(true);
                editTask.setEnabled(true);
                closeTask.setEnabled(true);
                deleteTask.setEnabled(true);
                startWork.setEnabled(true);
                showAutoTask.setEnabled(true);
                showCloseTask.setEnabled(true);
            }
            timeField.setText("");
        });


        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(AMOUNT_BUTTONS, AMOUNT_COLUMNS));
        buttonPanel.add(addTask);
        buttonPanel.add(editTask);
        buttonPanel.add(closeTask);
        buttonPanel.add(deleteTask);
        buttonPanel.add(showAutoTask);
        buttonPanel.add(showCloseTask);
        buttonPanel.add(startWork);
        buttonPanel.add(endWork);
        buttonPanel.add(timeField);
    }

    private void createTaskTable() {
        table = new TaskTable(controller.selectActiveTask());
        taskTable = new JTable(table);
        setDefaultSizeTable();
    }

    private void updateTaskTable() {
        table.refreshTable(controller.selectActiveTask());
        setDefaultSizeTable();
        taskTable.repaint();
        taskTable.revalidate();
    }

    private void setDefaultSizeTable() {
        taskTable.setRowHeight(heightFrame / 15);
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(widthFrame / 2);
    }

    private void createView() {
        setLayout(new BorderLayout());
        JPanel flowLayoutButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        flowLayoutButtonPanel.add(buttonPanel);
        add(flowLayoutButtonPanel, BorderLayout.EAST);
        add(new JScrollPane(taskTable), BorderLayout.CENTER);
    }
}
