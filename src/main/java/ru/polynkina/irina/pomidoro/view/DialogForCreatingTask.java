package ru.polynkina.irina.pomidoro.view;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.GenerationType;
import ru.polynkina.irina.pomidoro.model.Priority;
import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Properties;

public class DialogForCreatingTask extends JDialog {

    private static final int AMOUNT_ROWS = 6;
    private static final int AMOUNT_COLUMNS = 2;

    private JLabel description;
    private JLabel priority;
    private JLabel type;
    private JLabel startDate;
    private JLabel endDate;

    private JTextArea descriptionArea;
    private JComboBox priorityBox;
    private JComboBox typeBox;
    private JLabel startDateText;
    private JDatePickerImpl endDatePicker;

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

        startDate = new JLabel("Дата начала задачи");
        startDate.setHorizontalAlignment(JLabel.CENTER);
        startDateText = new JLabel();
        startDateText.setText(LocalDate.now().toString());

        endDate = new JLabel("Дата завершения задачи");
        endDate.setHorizontalAlignment(JLabel.CENTER);

        Properties properties = new Properties();
        properties.put("text.today", "Сегодня");
        UtilDateModel endModel = new UtilDateModel();
        endModel.setDate(LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1, LocalDate.now().getDayOfMonth());
        endModel.setSelected(true);
        JDatePanelImpl endDatePanel = new JDatePanelImpl(endModel, properties);
        endDatePicker = new JDatePickerImpl(endDatePanel, new DateLabelFormatter());

        ok = new JButton("Добавить");
        ok.addActionListener(e -> {
            String taskDescription = descriptionArea.getText();
            Priority taskPriority = Priority.getValueByIndex(priorityBox.getSelectedIndex());
            GenerationType taskType = GenerationType.getValueByIndex(typeBox.getSelectedIndex());
            LocalDate taskEndDay = LocalDate.parse(endDatePicker.getJFormattedTextField().getText());
            controller.addTask(new Task(taskDescription, taskPriority, taskType, taskEndDay));
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

        panel.add(startDate);
        panel.add(startDateText);

        panel.add(endDate);
        panel.add(endDatePicker);

        panel.add(ok);
        panel.add(cancel);

        add(panel);
        setVisible(false);
    }

    public boolean userActionsIsSuccessful() {
        return actionsIsSuccessful;
    }
}