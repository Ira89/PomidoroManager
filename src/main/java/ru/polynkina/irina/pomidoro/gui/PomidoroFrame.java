package ru.polynkina.irina.pomidoro.gui;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PomidoroFrame extends JFrame {

    private int widthFrame;
    private int heightFrame;

    private JPanel buttonPanel;
    private JPanel taskPanel;

    private JTable table;

    private Controller controller;

    public PomidoroFrame(List<Task> taskList, Controller controller) {
        super("Pomidoro Manager");
        this.controller = controller;

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        widthFrame = size.width / 2;
        heightFrame = size.height / 2;
        setSize(widthFrame, heightFrame );
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        table = new JTable(new TaskTable(taskList));
        table.setRowHeight(heightFrame / 15);
        table.getColumnModel().getColumn(0).setPreferredWidth(widthFrame / 2);

        buttonPanel = new ButtonPanel(controller);
        taskPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        taskPanel.add(buttonPanel);

        setLayout(new BorderLayout());
        add(taskPanel, BorderLayout.EAST);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
