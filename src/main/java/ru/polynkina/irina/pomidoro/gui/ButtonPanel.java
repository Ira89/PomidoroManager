package ru.polynkina.irina.pomidoro.gui;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {

    private static final int AMOUNT_BUTTONS = 5;
    private static final int AMOUNT_COLUMNS = 1;

    public ButtonPanel() {

        setLayout(new GridLayout(AMOUNT_BUTTONS, AMOUNT_COLUMNS));

        JButton addTask = new JButton("Добавить задачу");
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
