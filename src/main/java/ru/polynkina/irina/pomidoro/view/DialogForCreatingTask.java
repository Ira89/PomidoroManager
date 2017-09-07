package ru.polynkina.irina.pomidoro.view;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.GenerationType;
import ru.polynkina.irina.pomidoro.model.Priority;
import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DialogForCreatingTask extends JDialog {

    private static final int AMOUNT_ROWS = 5;
    private static final int AMOUNT_COLUMNS = 2;

    private JLabel description;
    private JLabel priority;
    private JLabel type;
    private JLabel endDate;

    private JTextArea descriptionArea;
    private JComboBox priorityBox;
    private JComboBox typeBox;
    private JTextArea endDateArea;

    private JButton ok;
    private JButton cancel;

    private boolean actionsIsSuccessful;


    public DialogForCreatingTask(JFrame owner, String name, Controller controller) {
        super(owner, name, true);
        initializeSizeDialog();
        createDialogElements(controller);
        createView();
    }

    private void initializeSizeDialog() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(size.width / 3, size.height / 2);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private void createDialogElements(Controller controller) {
        description = new JLabel("Описание задачи");
        description.setHorizontalAlignment(JLabel.CENTER);
        descriptionArea = new JTextArea("введите описание...");
        descriptionArea.setLineWrap(true);

        priority = new JLabel("Приоритет задачи");
        priority.setHorizontalAlignment(JLabel.CENTER);
        priorityBox = new JComboBox(Priority.values());

        type = new JLabel("Тип генерации");
        type.setHorizontalAlignment(JLabel.CENTER);
        typeBox = new JComboBox(GenerationType.getTextTypes());

        endDate = new JLabel("Дата завершения задачи");
        endDate.setHorizontalAlignment(JLabel.CENTER);
        endDateArea = new JTextArea("укажите дату завершения задачи...");

        ok = new JButton("Добавить");
        ok.addActionListener(e -> {
            String desc = descriptionArea.getText();
            Priority pr = Priority.getValueByIndex(priorityBox.getItemCount());
            GenerationType t = GenerationType.getValueByIndex(typeBox.getItemCount());
            // TODO
            LocalDate endDay = LocalDate.now();
            controller.insert(new Task(desc, pr, t, endDay));
            actionsIsSuccessful = true;
            dispose();
        });

        cancel = new JButton("Отменить");
        cancel.addActionListener(e -> dispose());
    }

    private void createView() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(AMOUNT_ROWS, AMOUNT_COLUMNS));

        panel.add(description);
        panel.add(descriptionArea);

        panel.add(priority);
        panel.add(priorityBox);

        panel.add(type);
        panel.add(typeBox);

        panel.add(endDate);
        panel.add(endDateArea);

        panel.add(ok);
        panel.add(cancel);

        add(panel);
        setVisible(false);
    }

    public boolean userActionsIsSuccessful() {
        return actionsIsSuccessful;
    }
}