package ru.polynkina.irina.pomidoro.view.okcanceldialogs;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;

public class DeleteTaskDialog extends OkCancelDialog {

    private Controller controller;
    private Task task;

    public DeleteTaskDialog(JFrame owner, String name, Controller controller, Task task) {
        super(owner, name, task);
        this.controller = controller;
        this.task = task;
    }

    @Override
    public void okAction() {
        controller.deleteTask(task);
    }

    @Override
    public void cancelAction() {}

    @Override
    public String setActionDescription() {
        return "удалить задачу";
    }
}
