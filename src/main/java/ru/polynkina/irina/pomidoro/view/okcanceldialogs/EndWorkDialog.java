package ru.polynkina.irina.pomidoro.view.okcanceldialogs;

import ru.polynkina.irina.pomidoro.PomidoroTimer;
import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;
import java.time.LocalTime;

public class EndWorkDialog extends OkCancelDialog {

    private Controller controller;
    private PomidoroTimer pomidoroTimer;
    private Thread thread;
    private Task task;

    public EndWorkDialog(JFrame owner, String name, Controller controller,
                         Task task, PomidoroTimer pomidoroTimer, Thread thread) {
        super(owner, name, task);
        this.controller = controller;
        this.task = task;
        this.pomidoroTimer = pomidoroTimer;
        this.thread = thread;
    }

    @Override
    public void okAction() {
        thread.interrupt();
        task.addWorkTime(LocalTime.ofSecondOfDay(pomidoroTimer.getAllTimeWork()));
        controller.updateWorkTime(task);
        pomidoroTimer.reset();
    }

    @Override
    public void cancelAction() {}

    @Override
    public String setActionDescription() {
        return "завершить работу над задачей";
    }
}
