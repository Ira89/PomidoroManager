package ru.polynkina.irina.pomidoro.gui;

import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TaskTable extends DefaultTableModel {

    private static Object[] headers = {"Описание", "Приоритет", "Окончание", "Время работы"};

    public TaskTable(List<Task> taskList) {
        refreshTable(taskList);
    }

    public void refreshTable(List<Task> taskList) {
        setColumnIdentifiers(headers);
        for(Task task : taskList) {
            addRow(task.getInfo());
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
