package ru.polynkina.irina.pomidoro.gui;

import ru.polynkina.irina.pomidoro.GenerationType;
import ru.polynkina.irina.pomidoro.Priority;

import javax.swing.*;
import java.awt.*;

public class DialogFrame extends JFrame {

    private static int widthDialogFrame;
    private static int heightDialogFrame;

    private JPanel panel;

    private JLabel description;
    private JLabel priority;
    private JLabel type;
    private JLabel endDate;

    private JTextArea descriptionArea;
    private JComboBox priorityBox;
    private JComboBox typeBox;
    private JTextArea endDateArea;

    private Button ok;
    private Button cancel;

    public DialogFrame(String name) {
        super(name);

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        widthDialogFrame = size.width / 3;
        heightDialogFrame = size.height / 2;
        setSize(widthDialogFrame, heightDialogFrame );
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

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

        panel.add(description);
        panel.add(new JScrollPane(descriptionArea));
        panel.add(priority);
        panel.add(priorityBox);
        panel.add(type);
        panel.add(typeBox);
        panel.add(endDate);
        panel.add(new JScrollPane(endDateArea));

        ok = new Button("Добавить");
        cancel = new Button("Отменить");
        panel.add(ok);
        panel.add(cancel);

        add(panel);
        setVisible(false);
    }
}
