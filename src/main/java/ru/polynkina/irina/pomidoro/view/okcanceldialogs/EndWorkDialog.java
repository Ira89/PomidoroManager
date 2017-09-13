package ru.polynkina.irina.pomidoro.view.okcanceldialogs;

import ru.polynkina.irina.pomidoro.PomidoroTimer;
import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;
import java.time.LocalTime;

public class EndWorkDialog extends OkCancelDialog {

    private Controller controller;
    private PomidoroTimer pomidoroTimer;
    private Timer timer;
    private Task task;

    public EndWorkDialog(JFrame owner, String name, Controller controller, Task task, PomidoroTimer pomidoroTimer, Timer timer) {
        super(owner, name, task);
        this.controller = controller;
        this.task = task;
        this.pomidoroTimer = pomidoroTimer;
        this.timer = timer;
    }

    @Override
    public void okAction() {
        timer.stop();
        task.addWorkTime(LocalTime.ofSecondOfDay(pomidoroTimer.getAllTimeWork()));
        controller.updateWorkTime(task);
        pomidoroTimer.reset();
    }

    @Override
    public void cancelAction() {}

    @Override
    public String setActionDescription() {
        return "хотите завершить работу над задачей";
    }
}
