package ru.polynkina.irina.pomidoro.controller;

import ru.polynkina.irina.pomidoro.model.GenerationType;
import ru.polynkina.irina.pomidoro.model.Priority;
import ru.polynkina.irina.pomidoro.model.Task;
import ru.polynkina.irina.pomidoro.db.DBManager;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private DBManager dbManager;

    public Controller(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public void insert(Task task) {
        try {
            dbManager.executeUpdate("INSERT INTO task (" +
                    "description, priority, type, start_date, end_date, time_work) " +
                    "VALUES(" + task.getTextForSQL() + ")");
            dbManager.executeUpdate("INSERT INTO active_task (id_task) " +
                    "VALUES(SELECT MAX(id) FROM task)");
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }

    public List<Task> selectActiveTask() {
        List<Task> taskList = new ArrayList<>();
        try {
            ResultSet resultSet = dbManager.executeQuery("SELECT * FROM task " +
                    "WHERE id IN (SELECT id_task FROM active_task) ORDER BY priority");
            while(resultSet.next()) taskList.add(parseTask(resultSet));
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
        return taskList;
    }

    public Task selectLastTask() {
        try {
            ResultSet resultSet = dbManager.executeQuery("SELECT * FROM task " +
                    "WHERE id = (SELECT MAX(id) FROM task)");
            if(resultSet.next()) return parseTask(resultSet);
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    private Task parseTask(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt(1);
        String description = resultSet.getString(2);
        Priority priority = Priority.valueOf(resultSet.getString(3));
        GenerationType type = GenerationType.valueOfEnum(resultSet.getString(4));
        LocalDate startDate = LocalDate.parse(resultSet.getDate(5).toString());
        LocalDate endDate = LocalDate.parse(resultSet.getDate(6).toString());
        LocalTime time = LocalTime.parse(resultSet.getTime(7).toString());
        return new Task(id, description, priority, type, startDate, endDate, time);
    }
}
