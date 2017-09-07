package ru.polynkina.irina.pomidoro;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.db.DBManager;
import ru.polynkina.irina.pomidoro.view.PomidoroFrame;

import java.awt.*;

public class Main {

    public static void main(String[] args) throws Exception {

        DBManager dbManager = new DBManager("dbtest");
        Controller controller = new Controller(dbManager);
        EventQueue.invokeAndWait(() -> new PomidoroFrame(controller));
    }
}
