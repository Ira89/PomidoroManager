package ru.polynkina.irina.pomidoro.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel {

    private static final int AMOUNT_BUTTONS = 5;
    private static final int AMOUNT_COLUMNS = 1;

    private DialogFrame dialogFrame;

    public ButtonPanel() {

        setLayout(new GridLayout(AMOUNT_BUTTONS, AMOUNT_COLUMNS));

        JButton addTask = new JButton("Добавить задачу");
        addTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogFrame = new DialogFrame("Добавление задачи");
                dialogFrame.setVisible(true);
            }
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
