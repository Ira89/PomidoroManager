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

    public void addTask(Task task) {
        System.out.println("add: " + task);
        try {
            dbManager.executeUpdate("INSERT INTO task (" +
                    "description, priority, type, start_date, end_date, time_work) " +
                    "VALUES(" + task.getTextForSQL() + ")");
            if(task.getType() == GenerationType.ONCE) {
                dbManager.executeUpdate("INSERT INTO active_task (id_task) " +
                        "VALUES(SELECT MAX(id) FROM task)");
            } else {
                dbManager.executeUpdate("INSERT INTO auto_task (id_task) " +
                        "VALUES(SELECT MAX(id) FROM task)");
            }
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }

    public void updateTask(Task task) {
        System.out.println("update: " + task);
        try {
            ResultSet resultSet = dbManager.executeQuery("SELECT type FROM task WHERE id = " + task.getId());
            resultSet.next();
            GenerationType oldType = GenerationType.valueOfEnum(resultSet.getString(1));

            dbManager.executeUpdate("UPDATE task SET description = '" + task.getDescription() +
            "', priority = '" + task.getPriority() + "', type = '" + task.getType() +
            "', start_date = '" + task.getStartDay() + "', end_date = '" + task.getEndDay() +
            "' WHERE id = " + task.getId());

            if(oldType != task.getType()) {
                dbManager.executeUpdate("DELETE FROM active_task WHERE id_task = " + task.getId());
                dbManager.executeUpdate("INSERT INTO auto_task (id_task) VALUES(" + task.getId() + ")");
            }

        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }

    public void deleteTask(Task task) {
        System.out.println("delete: " + task);
        try {
            ResultSet resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id = " + task.getId());
            if(resultSet.next()) {
                dbManager.executeUpdate("DELETE FROM active_task WHERE id_task = " + task.getId());
                dbManager.executeUpdate("DELETE FROM task WHERE id = " + task.getId());
            } else {
                // авто-таск безвозвратно не удаляем, инфо оставляем для истории
                dbManager.executeUpdate("DELETE FROM active_task WHERE id_task = " + task.getId());
                dbManager.executeUpdate("DELETE FROM auto_task WHERE id_task = " + task.getId());
                dbManager.executeUpdate("INSERT INTO close_task (id_task) VALUES (" + task.getId() + ")");
            }
            resultSet.close();
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }

    public void closeTask(Task task) {
        System.out.println("close: " + task);
        try {
            dbManager.executeUpdate("DELETE FROM active_task WHERE id_task = " + task.getId());
            dbManager.executeUpdate("INSERT INTO close_task (id_task) VALUES(" + task.getId() + ")");
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
            resultSet.close();
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
        return taskList;
    }

    public List<Task> selectCloseTask() {
        List<Task> taskList = new ArrayList<>();
        try {
            ResultSet resultSet = dbManager.executeQuery("SELECT * FROM task " +
                    "WHERE id IN (SELECT id_task FROM close_task) ORDER BY priority");
            while(resultSet.next()) taskList.add(parseTask(resultSet));
            resultSet.close();
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
        return taskList;
    }

    public List<Task> selectAutoTask() {
        List<Task> taskList = new ArrayList<>();
        try {
            ResultSet resultSet = dbManager.executeQuery("SELECT * FROM task " +
                    "WHERE id IN (SELECT id_task FROM auto_task) ORDER BY priority");
            while(resultSet.next()) taskList.add(parseTask(resultSet));
            resultSet.close();
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
        return taskList;
    }

    public void generateAutoTasks() {
        try {
            ResultSet resultSet = dbManager.executeQuery("SELECT task.id FROM task, auto_task " +
                    "WHERE task.id = auto_task.id_task " +
                    "AND task.start_date = " + "'" + LocalDate.now() + "'");
            while(resultSet.next()) {
                dbManager.executeUpdate("INSERT INTO active_task (id_task) VALUES(" + resultSet.getInt(1) + ")");
            }
            resultSet.close();
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
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
