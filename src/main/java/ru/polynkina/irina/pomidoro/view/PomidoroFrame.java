package ru.polynkina.irina.pomidoro.view;

import ru.polynkina.irina.pomidoro.PomidoroTimer;
import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.Task;
import ru.polynkina.irina.pomidoro.view.addEditDialogs.AddTaskDialog;
import ru.polynkina.irina.pomidoro.view.addEditDialogs.EditTaskDialog;
import ru.polynkina.irina.pomidoro.view.okCancelDialogs.CloseTaskDialog;
import ru.polynkina.irina.pomidoro.view.okCancelDialogs.DeleteTaskDialog;
import ru.polynkina.irina.pomidoro.view.okCancelDialogs.EndWorkDialog;
import ru.polynkina.irina.pomidoro.view.okCancelDialogs.StartWorkDialog;
import ru.polynkina.irina.pomidoro.view.taskTable.TaskTable;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PomidoroFrame extends JFrame {

    private static final int AMOUNT_BUTTONS = 10;
    private static final int AMOUNT_COLUMNS = 1;

    private Controller controller;
    private Task taskForWork;

    private int widthFrame;
    private int heightFrame;

    private JPanel buttonPanel;
    private JTable taskTable;
    private TaskTable table;

    private JButton addTask;
    private JButton editTask;
    private JButton closeTask;
    private JButton deleteTask;
    private JButton startWork;
    private JButton endWork;
    private JButton showAutoTask;
    private JButton showCloseTask;
    private JButton version;

    private PomidoroTimer pomidoroTimer;
    private JTextField timeField;
    private Thread thread;


    public PomidoroFrame(Controller controller) {
        super("Pomidoro Manager");
        this.controller = controller;
        addWindowListener(new SaveListener());

        timeField = new JTextField();
        timeField.setEnabled(false);
        timeField.setDisabledTextColor(Color.BLUE);
        pomidoroTimer = new PomidoroTimer(timeField);

        initializeSizeFrame();
        createButtonPanel();
        createTaskTable();
        createView();
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
        addTask = new JButton("Добавить задачу");
        addTask.addActionListener(e -> {
            AddTaskDialog dialogFrame = new AddTaskDialog(PomidoroFrame.this,"Добавление задачи", controller);
            dialogFrame.setVisible(true);
            if(dialogFrame.userActionsIsSuccessful()) updateTaskTable();
        });

        editTask = new JButton("Редактировать задачу");
        editTask.addActionListener(e -> {
            EditTaskDialog dialog = new EditTaskDialog(PomidoroFrame.this,
                    "Редактирование задачи", controller, table.getTask(taskTable.getSelectedRow()));
            dialog.setVisible(true);
            if(dialog.userActionsIsSuccessful()) updateTaskTable();
        });

        closeTask = new JButton("Закрыть задачу");
        closeTask.addActionListener(e -> {
            CloseTaskDialog dialog = new CloseTaskDialog(PomidoroFrame.this,
                    "Закрытие задачи", controller, table.getTask(taskTable.getSelectedRow()));
            dialog.setVisible(true);
            if(dialog.userActionsIsSuccessful()) updateTaskTable();
        });

        deleteTask = new JButton("Удалить задачу");
        deleteTask.addActionListener(e -> {
            DeleteTaskDialog dialog = new DeleteTaskDialog(PomidoroFrame.this,
                    "Удаление задачи", controller, table.getTask(taskTable.getSelectedRow()));
            dialog.setVisible(true);
            if(dialog.userActionsIsSuccessful()) updateTaskTable();
        });

        showAutoTask = new JButton("Авто-задачи");
        showAutoTask.addActionListener(e -> {
            AutoTaskFrame autoTaskFrame = new AutoTaskFrame(PomidoroFrame.this, controller);
            autoTaskFrame.setVisible(true);
        });

        showCloseTask = new JButton("Закрытые задачи");
        showCloseTask.addActionListener(e -> {
            CloseTaskFrame closeTaskFrame = new CloseTaskFrame(PomidoroFrame.this, controller);
            closeTaskFrame.setVisible(true);
        });

        version = new JButton("О программе");
        version.addActionListener(e -> {
            InfoFrame infoFrame = new InfoFrame(null, "О программе",
                    "version: beta" +
                            "<br>release: 18/09/2017" +
                            "<br>author: Irina Polynkina" +
                            "<br>email: irina.polynkina.dev@yandex.ru");
            infoFrame.setVisible(true);
        });

        startWork = new JButton("Работать над задачей");
        startWork.addActionListener((ActionEvent e) -> {
            taskForWork = table.getTask(taskTable.getSelectedRow());
            if(taskForWork != null) {
                thread = new Thread(pomidoroTimer);
                StartWorkDialog dialog = new StartWorkDialog(PomidoroFrame.this, "Начать работу", taskForWork, thread);
                dialog.setVisible(true);
                if(dialog.userActionsIsSuccessful()) makeButtonsActive(false);
            }
        });

        endWork = new JButton("Остановить работу");
        endWork.addActionListener(e -> {
            EndWorkDialog dialog = new EndWorkDialog(PomidoroFrame.this, "Завершить работу",
                    controller, taskForWork, pomidoroTimer, thread);
            dialog.setVisible(true);
            if(dialog.userActionsIsSuccessful()) {
                updateTaskTable();
                makeButtonsActive(true);
                taskForWork = null;
                timeField.setText("");
            }
        });

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(AMOUNT_BUTTONS, AMOUNT_COLUMNS));
        buttonPanel.add(addTask);
        buttonPanel.add(editTask);
        buttonPanel.add(closeTask);
        buttonPanel.add(deleteTask);
        buttonPanel.add(showAutoTask);
        buttonPanel.add(showCloseTask);
        buttonPanel.add(version);
        buttonPanel.add(startWork);
        buttonPanel.add(endWork);
        buttonPanel.add(timeField);
    }

    private void createTaskTable() {
        table = new TaskTable(controller.selectActiveTask());
        taskTable = new JTable(table);
        taskTable.setRowSorter(new TableRowSorter<>(table));
        setDefaultSizeTable();
    }

    private void updateTaskTable() {
        controller.generateAutoTasks();
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

    private void makeButtonsActive(boolean isActive) {
        addTask.setEnabled(isActive);
        editTask.setEnabled(isActive);
        closeTask.setEnabled(isActive);
        deleteTask.setEnabled(isActive);
        startWork.setEnabled(isActive);
        version.setEnabled(isActive);
        showAutoTask.setEnabled(isActive);
        showCloseTask.setEnabled(isActive);
    }

    @Override
    public synchronized void addWindowListener(WindowListener l) {
        super.addWindowListener(new SaveListener());
    }

    private class SaveListener implements WindowListener {
        @Override
        public void windowClosing(WindowEvent e) {
            Rectangle frameBounds = getBounds();
            Properties properties = new Properties();
            properties.setProperty("xPos", String.valueOf(frameBounds.x));
            properties.setProperty("yPos", String.valueOf(frameBounds.y));
            properties.setProperty("width", String.valueOf(frameBounds.width));
            properties.setProperty("height", String.valueOf(frameBounds.height));

            try {
                FileOutputStream output = new FileOutputStream(
                        System.getProperty("user.home") + File.separator + "pomidoro.properties");
                properties.store(output, "Saved settings");
                output.close();
            } catch(Exception exc) {
                exc.printStackTrace();
            }

            controller.closeDB();
            System.exit(0);
        }

        public void windowOpened(WindowEvent e) {}
        public void windowClosed(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowActivated(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) {}
    }
}