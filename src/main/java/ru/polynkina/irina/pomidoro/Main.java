package ru.polynkina.irina.pomidoro;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.db.DBManager;
import ru.polynkina.irina.pomidoro.view.PomidoroFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Main {

    private static void fillDB(DBManager dbManager) throws SQLException {
        /*
        dbManager.executeUpdate("INSERT INTO task VALUES(" + "1, 'сделать abc', 'A', 'ONCE', '2017-08-15', '2017-08-30', '00:00:00')");
        dbManager.executeUpdate("INSERT INTO task VALUES(" + "2, 'сделать x', 'B', 'ONCE', '2017-08-30', '2017-08-30', '13:00:00')");
        dbManager.executeUpdate("INSERT INTO task VALUES(" + "3, 'сделать y', 'C', 'ONCE', '2017-09-15', '2017-08-30', '00:50:00')");
        dbManager.executeUpdate("INSERT INTO task VALUES(" + "4, 'сделать z', 'D', 'ONCE', '2017-09-30', '2017-08-30', '16:10:00')");
        dbManager.executeUpdate("INSERT INTO task VALUES(" + "5, 'длинное-длинное описание задачи', 'A', 'ONCE', '2017-08-15', '2017-08-30', '00:00:15')");
        */
    }

    public static void main(String[] args) throws Exception {

        DBManager dbManager = new DBManager("dbtest");
        Controller controller = new Controller(dbManager);

        try {
            try {
                fillDB(dbManager);
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

        EventQueue.invokeAndWait(() -> {
            JFrame frame = new PomidoroFrame(controller);
        });
    }
}
