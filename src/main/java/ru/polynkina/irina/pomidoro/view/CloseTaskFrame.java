package ru.polynkina.irina.pomidoro.view;

import ru.polynkina.irina.pomidoro.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class CloseTaskFrame extends JDialog {

    private int widthFrame;
    private JTable taskTable;

    public CloseTaskFrame(JFrame owner, Controller controller) {
        super(owner, "Закрытые задачи", true);

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        widthFrame = size.width / 2;

        setBounds(owner.getX(), owner.getY(), owner.getWidth(), owner.getHeight());

        taskTable = new JTable(new TaskTable(controller.selectCloseTask()));
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(widthFrame / 2);
        add(new JScrollPane(taskTable));
    }
}
