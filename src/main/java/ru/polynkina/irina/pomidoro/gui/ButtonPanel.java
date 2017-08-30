package ru.polynkina.irina.pomidoro.gui;

import ru.polynkina.irina.pomidoro.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {

    private static final int AMOUNT_BUTTONS = 5;
    private static final int AMOUNT_COLUMNS = 1;

    private DialogFrame dialogFrame;

    public ButtonPanel(Controller controller) {

        setLayout(new GridLayout(AMOUNT_BUTTONS, AMOUNT_COLUMNS));

        JButton addTask = new JButton("Добавить задачу");
        addTask.addActionListener(e -> {
            dialogFrame = new DialogFrame("Добавление задачи", controller);
            dialogFrame.setVisible(true);
        });

        JButton editTask = new JButton("Редактировать задачу");
        JButton closeTask = new JButton("Закрыть задачу");
        JButton deleteTask = new JButton("Удалить задачу");
        JButton startWork = new JButton("Начать работу над задачей");

        add(addTask);
        add(editTask);
        add(closeTask);
        add(deleteTask);
        add(startWork);
    }
}
