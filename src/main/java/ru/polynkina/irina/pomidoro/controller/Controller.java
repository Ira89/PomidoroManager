package ru.polynkina.irina.pomidoro.controller;

import ru.polynkina.irina.pomidoro.model.GenerationType;
import ru.polynkina.irina.pomidoro.model.Priority;
import ru.polynkina.irina.pomidoro.model.Task;
import ru.polynkina.irina.pomidoro.db.DBManager;

import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Controller {

    private DBManager dbManager;

    public Controller(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public void insert(Task task) {
        try {
            dbManager.executeUpdate("INSERT INTO task (" +
                    "description, priority, type, start_date, end_date, time_work)" +
                    "VALUES" + task.getTextForSQL());
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }

    public List<Task> selectAll() {
        List<Task> taskList = new ArrayList<>();
        try {
            ResultSet result = dbManager.executeQuery("SELECT * FROM task ORDER BY priority");
            while(result.next()) {
                int id = result.getInt(1);
                String description = result.getString(2);
                Priority priority = Priority.valueOf(result.getString(3));
                GenerationType type = GenerationType.valueOfEnum(result.getString(4));
                Date startDate = result.getDate(5);
                Date endDate = result.getDate(6);
                Time time = result.getTime(7);
                // TODO
                taskList.add(new Task(id, description, priority, type, LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.getDay())));
            }
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
        return taskList;
    }

    public Task selectLastTask() {
        try {
            ResultSet result = dbManager.executeQuery("SELECT * FROM task WHERE id = (SELECT MAX(id) FROM task)");
            if(result.next()) {
                int id = result.getInt(1);
                String description = result.getString(2);
                Priority priority = Priority.valueOf(result.getString(3));
                GenerationType type = GenerationType.valueOfEnum(result.getString(4));
                Date startDate = result.getDate(5);
                Date endDate = result.getDate(6);
                Time time = result.getTime(7);
                // TODO
                return new Task(id, description, priority, type, LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.getDay()));
            }
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
}
