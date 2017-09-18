package ru.polynkina.irina.pomidoro.view;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.view.taskTable.TaskTable;

import javax.swing.*;
import java.awt.*;

public class CloseTaskFrame extends JDialog {

    public CloseTaskFrame(JFrame owner, Controller controller) {
        super(owner, "Закрытые задачи", true);
        setBounds(owner.getX(), owner.getY(), owner.getWidth(), owner.getHeight());
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        JTable taskTable = new JTable(new TaskTable(controller.selectCloseTask()));
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(size.width / 4);
        add(new JScrollPane(taskTable));
    }
}