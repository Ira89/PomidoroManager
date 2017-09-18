package ru.polynkina.irina.pomidoro.view.addEditDialogs;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;

public class EditTaskDialog extends AddEditDialog {

    private Controller controller;

    public EditTaskDialog(JFrame owner, String name, Controller controller, Task task) {
        super(owner, name, task);
        this.controller = controller;
    }

    @Override
    public void okAction() { ;
        controller.updateTask(formTask());
    }

    @Override
    public void cancelAction() {}
}
