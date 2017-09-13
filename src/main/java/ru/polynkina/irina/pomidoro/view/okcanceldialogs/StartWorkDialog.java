package ru.polynkina.irina.pomidoro.view.okcanceldialogs;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;

public class StartWorkDialog extends OkCancelDialog {

    private Controller controller;
    private Timer timer;

    public StartWorkDialog(JFrame owner, String name, Controller controller, Task task, Timer timer) {
        super(owner, name, task);
        this.controller = controller;
        this.timer = timer;
    }

    @Override
    public void okAction() {
        timer.start();
    }

    @Override
    public void cancelAction() {}

    @Override
    public String setActionDescription() {
        return "начать работу над задачей";
    }
}
