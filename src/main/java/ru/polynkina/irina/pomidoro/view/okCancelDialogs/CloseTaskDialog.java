package ru.polynkina.irina.pomidoro.view.okCancelDialogs;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;

public class CloseTaskDialog extends OkCancelDialog {

    private Controller controller;
    private Task task;

    public CloseTaskDialog(JFrame owner, String name, Controller controller, Task task) {
        super(owner, name, task);
        this.controller = controller;
        this.task = task;
    }

    @Override
    public void okAction() {
        controller.closeTask(task);
    }

    @Override
    public void cancelAction() {}

    @Override
    public String setActionDescription() {
        return "завершить работу над задачей";
    }
}
