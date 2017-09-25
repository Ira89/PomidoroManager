package ru.polynkina.irina.pomidoro.view.addEditDialogs;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import ru.polynkina.irina.pomidoro.model.GenerationType;
import ru.polynkina.irina.pomidoro.model.Priority;
import ru.polynkina.irina.pomidoro.model.Task;
import ru.polynkina.irina.pomidoro.view.dateFormatter.DateLabelFormatter;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Properties;

public abstract class AddEditDialog extends JDialog {

    private static final int AMOUNT_ROWS = 9;
    private static final int AMOUNT_COLUMNS = 2;

    private JLabel description;
    private JLabel priority;
    private JLabel type;
    private JLabel endDate;

    private JTextPane descriptionArea;
    private JComboBox priorityBox;
    private JComboBox typeBox;
    private JDatePickerImpl endDatePicker;

    private JButton ok;
    private JButton cancel;

    private Task task;
    private boolean actionsIsSuccessful;


    public AddEditDialog(JFrame owner, String name, Task task) {
        super(owner, name, true);
        this.task = task;
        initializeSizeDialog();
        createDialogElements();
        createView();
    }

    public abstract void okAction();

    public abstract void cancelAction();

    private void initializeSizeDialog() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(size.width / 3, size.height / 2);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private void createDialogElements() {
        description = new JLabel("Описание задачи");
        description.setHorizontalAlignment(JLabel.CENTER);
        descriptionArea = new JTextPane();
        descriptionArea.setText(task.getDescription());

        priority = new JLabel("Приоритет задачи");
        priority.setHorizontalAlignment(JLabel.CENTER);
        priorityBox = new JComboBox(Priority.values());
        priorityBox.setSelectedIndex(Priority.getIndexByValue(task.getPriority()));

        type = new JLabel("Тип генерации");
        type.setHorizontalAlignment(JLabel.CENTER);
        typeBox = new JComboBox(GenerationType.getTextTypes());
        typeBox.setSelectedIndex(GenerationType.getIndexByValue(task.getType()));
        typeBox.addActionListener(e -> {
            GenerationType currentType = GenerationType.getValueByIndex(typeBox.getSelectedIndex());
            if(currentType != GenerationType.ONCE) endDate.setText("Начать генерацию с");
            else endDate.setText("Завершить задачу до");
        });

        endDate = new JLabel("Завершить задачу до");
        endDate.setHorizontalAlignment(JLabel.CENTER);

        Properties properties = new Properties();
        properties.put("text.today", "Сегодня");
        UtilDateModel endModel = new UtilDateModel();
        endModel.setDate(LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1, LocalDate.now().getDayOfMonth());
        endModel.setSelected(true);
        JDatePanelImpl endDatePanel = new JDatePanelImpl(endModel, properties);
        endDatePicker = new JDatePickerImpl(endDatePanel, new DateLabelFormatter());

        ok = new JButton("Ок");
        ok.addActionListener(e -> {
            actionsIsSuccessful = true;
            okAction();
            dispose();
        });

        cancel = new JButton("Отмена");
        cancel.addActionListener(e -> {
            actionsIsSuccessful = false;
            cancelAction();
            dispose();
        });
    }

    protected Task formTask() {
        int id = task.getId();
        String taskDescription = descriptionArea.getText();
        Priority taskPriority = Priority.getValueByIndex(priorityBox.getSelectedIndex());
        GenerationType taskType = GenerationType.getValueByIndex(typeBox.getSelectedIndex());
        LocalDate startDay;
        LocalDate endDay;
        if(taskType == GenerationType.ONCE) {
            startDay = LocalDate.now();
            endDay = LocalDate.parse(endDatePicker.getJFormattedTextField().getText());
        } else {
            startDay = LocalDate.parse(endDatePicker.getJFormattedTextField().getText());
            endDay = LocalDate.of(9999, 12, 31);
        }
        return new Task(id, taskDescription, taskPriority, taskType, startDay, endDay);
    }

    private void createView() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(AMOUNT_ROWS, AMOUNT_COLUMNS));

        panel.add(description);
        panel.add(new JScrollPane(descriptionArea));
        panel.add(new JSeparator());
        panel.add(new JSeparator());

        panel.add(priority);
        panel.add(priorityBox);
        panel.add(new JSeparator());
        panel.add(new JSeparator());

        panel.add(type);
        panel.add(typeBox);
        panel.add(new JSeparator());
        panel.add(new JSeparator());

        panel.add(endDate);
        panel.add(endDatePicker);
        panel.add(new JSeparator());
        panel.add(new JSeparator());

        panel.add(ok);
        panel.add(cancel);

        add(panel);
        setVisible(false);
    }

    public boolean userActionsIsSuccessful() {
        return actionsIsSuccessful;
    }
}