package ru.polynkina.irina.pomidoro.view.addEditDialogs;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.GenerationType;
import ru.polynkina.irina.pomidoro.model.Priority;
import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;
import java.time.LocalDate;

public class  AddTaskDialog extends AddEditDialog {

    private Controller controller;

    public AddTaskDialog(JFrame owner, String name, Controller controller) {
        super(owner, name, createDefaultTask());
        this.controller = controller;
    }

    private static Task createDefaultTask() {
        return new Task("Введите описание задачи", Priority.D, GenerationType.ONCE, LocalDate.now());
    }

    @Override
    public void okAction() {
        controller.addTask(formTask());
    }

    @Override
    public void cancelAction() {}
}
