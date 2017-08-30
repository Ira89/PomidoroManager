package ru.polynkina.irina.pomidoro.db;

import java.sql.*;

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
                connection = DriverManager.getConnection(url + dbName + ";create=true");
            } catch(ClassNotFoundException | SQLException exc) {
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

    public void executeUpdate(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    public void insert(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);
        statement.close();
    }
}
