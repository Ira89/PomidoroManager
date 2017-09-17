package ru.polynkina.irina.pomidoro;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.db.DBManager;
import ru.polynkina.irina.pomidoro.view.PomidoroFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws Exception {

        DBManager dbManager = new DBManager("dbpomidoro");
        Controller controller = new Controller(dbManager);
        controller.generateAutoTasks();
        SwingUtilities.invokeLater(() -> new PomidoroFrame(controller));
    }
}
