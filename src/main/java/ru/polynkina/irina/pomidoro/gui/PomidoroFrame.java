package ru.polynkina.irina.pomidoro.gui;

import ru.polynkina.irina.pomidoro.Task;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PomidoroFrame extends JFrame {

    private static int widthFrame;
    private static int heightFrame;

    private static JPanel buttonPanel;
    private static JPanel taskPanel;

    private static JTable table;

    public PomidoroFrame(List<Task> taskList) {
        super("Pomidoro Manager");

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

        buttonPanel = new ButtonPanel();
        taskPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        taskPanel.add(buttonPanel);

        setLayout(new BorderLayout());
        add(taskPanel, BorderLayout.EAST);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
