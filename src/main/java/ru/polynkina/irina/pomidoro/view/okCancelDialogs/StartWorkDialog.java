package ru.polynkina.irina.pomidoro.view.okCancelDialogs;

import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;

public class StartWorkDialog extends OkCancelDialog {

    private Thread thread;

    public StartWorkDialog(JFrame owner, String name, Task task, Thread thread) {
        super(owner, name, task);
        this.thread = thread;
    }

    @Override
    public void okAction() {
        thread.start();
    }

    @Override
    public void cancelAction() {}

    @Override
    public String setActionDescription() {
        return "начать работу над задачей";
    }
}
