package ru.polynkina.irina.pomidoro.db;

import ru.polynkina.irina.pomidoro.model.GenerationType;
import ru.polynkina.irina.pomidoro.model.Priority;
import ru.polynkina.irina.pomidoro.model.Task;

import java.sql.*;
import java.time.LocalDate;

public class DBManager {

    private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String url = "jdbc:derby:";

    private String dbName;
    private Connection connection;

    public DBManager(String dbName) {
        this.dbName = dbName;
        if(!dbExist()) {
            try {
                Class.forName(driver);
                // Create the database with the command: ";create=true"
                connection = DriverManager.getConnection(url + this.dbName + ";create=true");
            } catch(ClassNotFoundException | SQLException exc) {
                exc.printStackTrace();
            }
        }
        if(!tablesExist()) {
            try {
                createTaskTable();
                createActiveTaskTable();
                createCloseTaskTable();
                createAutoTaskTable();
                addFictitiousTask();
            } catch(SQLException exc) {
                exc.printStackTrace();
            }
        }
    }

    private boolean dbExist() {
        boolean dbExist = false;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url + dbName);
            dbExist = true;
        } catch(Exception exc) {
            // Ignore it. Database does not exist.
        }
        return dbExist;
    }

    private boolean tablesExist() {
        boolean tableExist = false;
        try {
            ResultSet resultSet = executeQuery("SELECT * FROM task ORDER BY priority");
            if(resultSet.next()) tableExist = true;
        } catch(Exception exc) {
            // Ignore it. Tables does not exist.
        }
        return tableExist;
    }

    private void createTaskTable() throws SQLException {
        executeUpdate("CREATE TABLE task(" +
                "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL," +
                "description CHAR(128)," +
                "priority CHAR(1)," +
                "type CHAR(12)," +
                "start_date DATE," +
                "end_date DATE," +
                "time_work TIME," +
                "PRIMARY KEY(id))");
    }

    private void createActiveTaskTable() throws SQLException {
        executeUpdate("CREATE TABLE active_task(" +
                "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL," +
                "id_task INTEGER," +
                "FOREIGN KEY(id_task) REFERENCES task(id))");
    }

    private void createCloseTaskTable() throws SQLException {
        executeUpdate("CREATE TABLE close_task(" +
                "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL," +
                "id_task INTEGER," +
                "FOREIGN KEY(id_task) REFERENCES task(id))");
    }

    private void createAutoTaskTable() throws SQLException {
        executeUpdate("CREATE TABLE auot_task(" +
                "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL," +
                "id_task INTEGER," +
                "FOREIGN KEY(id_task) REFERENCES task(id))");
    }

    private void addFictitiousTask() throws SQLException {
        Task task = new Task("сделать ABC", Priority.C, GenerationType.ONCE, LocalDate.now());
        executeUpdate("INSERT INTO task (" +
                "description, priority, type, start_date, end_date, time_work)" +
                "VALUES" + task.getTextForSQL());
    }

    public void executeUpdate(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }
}
