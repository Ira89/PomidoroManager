package ru.polynkina.irina.pomidoro.view;

import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TaskTable extends DefaultTableModel {

    private static final Object[] headers = {"Описание", "Приоритет", "Окончание", "Время работы"};

    private List<Task> taskList;

    public TaskTable(List<Task> taskList) {
        refreshTable(taskList);
    }

    public void refreshTable(List<Task> taskList) {
        setRowCount(0);
        this.taskList = taskList;
        setColumnIdentifiers(headers);
        for(Task task : taskList) {
            addRow(task.getInfo());
        }
    }

    public Task getTask(int row) {
        return taskList.get(row);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
