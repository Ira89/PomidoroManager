package ru.polynkina.irina.pomidoro.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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

    private static final Logger LOGGER = LogManager.getLogger(Controller.class.getSimpleName());
    private DBManager dbManager;

    public Controller(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public void addTask(Task task) {
        try {
            dbManager.executeUpdate("INSERT INTO task (" +
                    "description, priority, type, start_date, end_date, time_work) " +
                    "VALUES(" + task.getTextForSQL() + ")");

            ResultSet resultSet = dbManager.executeQuery("SELECT MAX(id) FROM task");
            resultSet.next();
            int idTask = resultSet.getInt(1);
            task.setId(idTask);
            LOGGER.info("add: " + task);

            if(task.getType() == GenerationType.ONCE) {
                dbManager.executeUpdate("INSERT INTO active_task (id_task) VALUES(" + idTask + ")");
                LOGGER.info("id=" + task.getId() + " inserting into the active_task table");
            } else {
                dbManager.executeUpdate("INSERT INTO auto_task (id_task) VALUES(" + idTask + ")");
                LOGGER.info("id=" + task.getId() + " inserting into the auto_task table");
            }
            resultSet.close();
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }

    public void updateTask(Task task) {
        try {
            ResultSet resultSet = dbManager.executeQuery("SELECT * FROM task WHERE id = " + task.getId());
            resultSet.next();
            Task oldTask = parseTask(resultSet);
            LOGGER.info("update: " + oldTask);

            dbManager.executeUpdate("UPDATE task SET description = '" + task.getDescription() +
            "', priority = '" + task.getPriority() + "', type = '" + task.getType() +
            "', start_date = '" + task.getStartDay() + "', end_date = '" + task.getEndDay() +
            "' WHERE id = " + task.getId());
            LOGGER.info("id=" + task.getId() + " new values: " + task);

            if(!oldTask.getType().equals(task.getType())) {
                if(task.getType().equals(GenerationType.ONCE)) {
                    dbManager.executeUpdate("DELETE FROM auto_task WHERE id_task = " + task.getId());
                    LOGGER.info("id=" + task.getId() + " deleting from the auto_task table");
                    dbManager.executeUpdate("INSERT INTO active_task (id_task) VALUES(" + task.getId() + ")");
                    LOGGER.info("id=" + task.getId() + " inserting into the active_task table");
                } else {
                    dbManager.executeUpdate("DELETE FROM active_task WHERE id_task = " + task.getId());
                    LOGGER.info("id=" + task.getId() + " deleting from the active_task table");
                    dbManager.executeUpdate("INSERT INTO auto_task (id_task) VALUES(" + task.getId() + ")");
                    LOGGER.info("id=" + task.getId() + " inserting into the auto_task table");
                }
            }
            resultSet.close();
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }

    public void deleteTask(Task task) {
        LOGGER.info("delete: " + task);
        try {
            ResultSet resultSet = dbManager.executeQuery("SELECT * FROM auto_task WHERE id_task = " + task.getId());
            if(!resultSet.next()) {
                dbManager.executeUpdate("DELETE FROM active_task WHERE id_task = " + task.getId());
                LOGGER.info("id=" + task.getId() + " deleting from the active_task table");
                dbManager.executeUpdate("DELETE FROM task WHERE id = " + task.getId());
                LOGGER.info("id=" + task.getId() + " deleting from the task table");
            } else {
                // авто-таск безвозвратно не удаляем, инфо оставляем для истории
                dbManager.executeUpdate("DELETE FROM active_task WHERE id_task = " + task.getId());
                LOGGER.info("id=" + task.getId() + " deleting from the active_task table");
                dbManager.executeUpdate("DELETE FROM auto_task WHERE id_task = " + task.getId());
                LOGGER.info("id=" + task.getId() + " deleting from the auto_task table");
                dbManager.executeUpdate("INSERT INTO close_task (id_task) VALUES (" + task.getId() + ")");
                LOGGER.info("id=" + task.getId() + " inserting into the close_task table");
            }
            resultSet.close();
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }

    public void closeTask(Task task) {
        LOGGER.info("close: " + task);
        try {
            dbManager.executeUpdate("DELETE FROM active_task WHERE id_task = " + task.getId());
            LOGGER.info("id=" + task.getId() + " deleting from the active_task table");
            dbManager.executeUpdate("INSERT INTO close_task (id_task) VALUES(" + task.getId() + ")");
            LOGGER.info("id=" + task.getId() + " inserting into the close_task table");
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }

    public void updateWorkTime(Task task) {
        try {
            ResultSet resultSet = dbManager.executeQuery("SELECT * FROM task WHERE id = " + task.getId());
            resultSet.next();
            Task taskOld = parseTask(resultSet);
            LOGGER.info("update time: " + taskOld);
            dbManager.executeUpdate("UPDATE task SET time_work = '" + task.getWorkTime() + "' WHERE id = " + task.getId());
            LOGGER.info("id=" + task.getId() + " new workTime: " + task.getWorkTime());
            resultSet.close();
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
        LOGGER.info("starts auto-generation of tasks");
        try {
            ResultSet resultSet = dbManager.executeQuery("SELECT task.id FROM task, auto_task " +
                    "WHERE task.id = auto_task.id_task " +
                    "AND task.start_date <= " + "'" + LocalDate.now() + "'");
            while(resultSet.next()) {
                int idTask = resultSet.getInt(1);
                dbManager.executeUpdate("INSERT INTO active_task (id_task) VALUES(" + idTask + ")");
                LOGGER.info("id=" + idTask + " inserting into the active_task table");
                resultSet = dbManager.executeQuery("SELECT type FROM task WHERE id = " + idTask);
                resultSet.next();
                GenerationType type = GenerationType.valueOfEnum(resultSet.getString(1));
                LocalDate nextDate = LocalDate.now();
                switch(type) {
                    case EVERY_DAY: nextDate = nextDate.plusDays(1); break;
                    case EVERY_WEEK: nextDate = nextDate.plusWeeks(1); break;
                    default: nextDate = nextDate.plusMonths(1); break;
                }
                dbManager.executeUpdate("UPDATE task SET start_date = '" + nextDate + "' WHERE id = " + idTask);
                LOGGER.info("id=" + idTask + " set start_date = " + nextDate);
            }
            resultSet.close();
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
        LOGGER.info("ends auto-generation of tasks");
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
