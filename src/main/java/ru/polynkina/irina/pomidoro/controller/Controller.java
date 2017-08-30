package ru.polynkina.irina.pomidoro.controller;

import ru.polynkina.irina.pomidoro.model.Task;
import ru.polynkina.irina.pomidoro.db.DBManager;

public class Controller {

    private DBManager dbManager;

    public Controller(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public void insert(Task task) {
        try {
            dbManager.insert("INSERT INTO task VALUES" + task.getTextForSQL());
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }
}
