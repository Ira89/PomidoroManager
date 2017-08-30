package ru.polynkina.irina.pomidoro;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.db.DBManager;
import ru.polynkina.irina.pomidoro.gui.PomidoroFrame;
import ru.polynkina.irina.pomidoro.model.GenerationType;
import ru.polynkina.irina.pomidoro.model.Priority;
import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    private static void fillDB(DBManager dbManager) throws SQLException {
        dbManager.executeUpdate("INSERT INTO task VALUES(" + "1, 'сделать abc', 'A', 'ONCE', '2017-08-15', '2017-08-30', '00:00:00')");
        dbManager.executeUpdate("INSERT INTO task VALUES(" + "2, 'сделать x', 'B', 'ONCE', '2017-08-30', '2017-08-30', '13:00:00')");
        dbManager.executeUpdate("INSERT INTO task VALUES(" + "3, 'сделать y', 'C', 'ONCE', '2017-09-15', '2017-08-30', '00:50:00')");
        dbManager.executeUpdate("INSERT INTO task VALUES(" + "4, 'сделать z', 'D', 'ONCE', '2017-09-30', '2017-08-30', '16:10:00')");
        dbManager.executeUpdate("INSERT INTO task VALUES(" + "5, 'длинное-длинное описание задачи', 'A', 'ONCE', '2017-08-15', '2017-08-30', '00:00:15')");
    }

    private static void readDateWithDB(DBManager dbManager, List<Task> taskList) throws Exception {
        ResultSet result = dbManager.executeQuery("SELECT * FROM task ORDER BY priority");
        while(result.next()) {
            long id = result.getLong(1);
            String description = result.getString(2);
            Priority priority = Priority.valueOf(result.getString(3));
            GenerationType type = GenerationType.valueOfEnum(result.getString(4));
            Date startDate = result.getDate(5);
            Date endDate = result.getDate(6);
            Time time = result.getTime(7);
            // TODO
            taskList.add(new Task(description, priority, type, LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.getDay())));
        }
    }

    public static void main(String[] args) throws Exception {

        DBManager dbManager = new DBManager("dbtest");
        Controller controller = new Controller(dbManager);
        List<Task> taskList = new ArrayList<>();

        try {
            try {
                //fillDB(dbManager);
                readDateWithDB(dbManager, taskList);
            } catch(SQLException exc) {
                dbManager.executeUpdate("CREATE TABLE task(" +
                        "id INTEGER NOT NULL," +
                        "description CHAR(128)," +
                        "priority CHAR(1)," +
                        "type CHAR(12)," +
                        "start_date DATE," +
                        "end_date DATE," +
                        "time_work TIME)");
                fillDB(dbManager);
            }
        } catch(SQLException exc) {
            exc.printStackTrace();
        }

        EventQueue.invokeAndWait(new Runnable() {
            public void run() {
                JFrame frame = new PomidoroFrame(taskList, controller);
            }
        });
    }
}
