package ru.polynkina.irina.pomidoro.unittests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import ru.polynkina.irina.pomidoro.db.DBManager;
import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.model.GenerationType;
import ru.polynkina.irina.pomidoro.model.Priority;
import ru.polynkina.irina.pomidoro.model.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ControllerTest {

    private static DBManager dbManager;
    private static Controller controller;
    private static ResultSet resultSet;

    @BeforeClass
    public static void init() throws SQLException {
        dbManager = new DBManager("dbtest");
        controller = new Controller(dbManager);
    }

    @AfterClass
    public static void drop() throws SQLException {
        resultSet.close();
        dbManager.executeUpdate("DROP TABLE auto_task");
        dbManager.executeUpdate("DROP TABLE active_task");
        dbManager.executeUpdate("DROP TABLE close_task");
        dbManager.executeUpdate("DROP TABLE task");
    }

    private static int getMaxIdFromTaskTable() throws SQLException {
        resultSet = dbManager.executeQuery("SELECT MAX(id) FROM task");
        resultSet.next();
        return resultSet.getInt(1);
    }

    @Test
    public void testAddTask() throws SQLException {
        Task task = new Task(1,"testAddTask", Priority.A, GenerationType.ONCE,
                LocalDate.now(), LocalDate.now(), LocalTime.of(23, 15, 56));
        controller.addTask(task);
        task.setId(getMaxIdFromTaskTable());
        List<Task> taskList = controller.selectActiveTask();
        Assert.assertTrue(taskList.size() == 1);
        Assert.assertTrue(taskList.get(0).equals(task));
        resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id_task = " + task.getId());
        Assert.assertTrue(resultSet.next());
        controller.deleteTask(task);
    }

    @Test
    public void testUpdateTask() throws SQLException {
        Task task = new Task(1,"testAddTask", Priority.A, GenerationType.ONCE,
                LocalDate.now(), LocalDate.now(), LocalTime.of(23, 15, 56));
        controller.addTask(task);
        task.setId(getMaxIdFromTaskTable());
        task.setDescription("testUpdateTask");
        task.setPriority(Priority.C);
        task.setType(GenerationType.EVERY_DAY);
        controller.updateTask(task);
        resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id_task = " + task.getId());
        Assert.assertFalse(resultSet.next());
        resultSet = dbManager.executeQuery("SELECT * FROM auto_task WHERE id_task = " + task.getId());
        Assert.assertTrue(resultSet.next());
        List<Task> taskList = controller.selectAutoTask();
        Assert.assertTrue(taskList.size() == 1);
        Assert.assertTrue(taskList.get(0).equals(task));
        controller.deleteTask(task);
    }

    @Test
    public void testUpdateAutoTask() throws SQLException {
        Task task = new Task(1,"testAddTask", Priority.A, GenerationType.EVERY_DAY,
                LocalDate.now(), LocalDate.now(), LocalTime.of(23, 15, 56));
        controller.addTask(task);
        task.setId(getMaxIdFromTaskTable());
        task.setDescription("testUpdateAutoTask");
        task.setPriority(Priority.C);
        task.setType(GenerationType.ONCE);
        controller.updateTask(task);
        resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id_task = " + task.getId());
        Assert.assertTrue(resultSet.next());
        resultSet = dbManager.executeQuery("SELECT * FROM auto_task WHERE id_task = " + task.getId());
        Assert.assertFalse(resultSet.next());
        List<Task> taskList = controller.selectActiveTask();
        Assert.assertTrue(taskList.size() == 1);
        Assert.assertTrue(taskList.get(0).equals(task));
        controller.deleteTask(task);
    }

    @Test
    public void testDeleteTask() throws SQLException {
        Task task = new Task(1,"testDeleteTask", Priority.A, GenerationType.ONCE,
                LocalDate.now(), LocalDate.now(), LocalTime.of(23, 15, 56));
        controller.addTask(task);
        task.setId(getMaxIdFromTaskTable());
        controller.deleteTask(task);
        resultSet = dbManager.executeQuery("SELECT * FROM task WHERE id = " + task.getId());
        Assert.assertFalse(resultSet.next());
        resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id_task = " + task.getId());
        Assert.assertFalse(resultSet.next());
    }

    @Test
    public void testDeleteAutoTask() throws SQLException {
        Task task = new Task(1,"testDeleteAutoTask", Priority.A, GenerationType.EVERY_DAY,
                LocalDate.now(), LocalDate.now(), LocalTime.of(23, 15, 56));
        controller.addTask(task);
        task.setId(getMaxIdFromTaskTable());
        controller.deleteTask(task);
        resultSet = dbManager.executeQuery("SELECT * FROM auto_task WHERE id_task = " + task.getId());
        Assert.assertFalse(resultSet.next());
        resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id_task = " + task.getId());
        Assert.assertFalse(resultSet.next());
        resultSet = dbManager.executeQuery("SELECT * FROM close_task WHERE id_task = " + task.getId());
        Assert.assertTrue(resultSet.next());
        dbManager.executeUpdate("DELETE FROM close_task WHERE id_task = " + task.getId());
    }

    @Test
    public void testCloseTask() throws SQLException {
        Task task = new Task(1,"testCloseTask", Priority.A, GenerationType.ONCE,
                LocalDate.now(), LocalDate.now(), LocalTime.of(23, 15, 56));
        controller.addTask(task);
        task.setId(getMaxIdFromTaskTable());
        controller.closeTask(task);
        resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id_task = " + task.getId());
        Assert.assertFalse(resultSet.next());
        resultSet = dbManager.executeQuery("SELECT * FROM close_task WHERE id_task = " + task.getId());
        Assert.assertTrue(resultSet.next());
    }

    @Test
    public void testUpdateWorkTime() throws SQLException {
        Task task = new Task(1,"testWorkTime", Priority.A, GenerationType.ONCE,
                LocalDate.now(), LocalDate.now(), LocalTime.of(23, 15, 56));
        controller.addTask(task);
        task.setId(getMaxIdFromTaskTable());
        task.addWorkTime(LocalTime.of(1, 59, 59));;
        controller.updateWorkTime(task);
        List<Task> taskList = controller.selectActiveTask();
        Assert.assertTrue(taskList.size() == 1);
        Assert.assertTrue(taskList.get(0).equals(task));
        controller.deleteTask(task);
    }

    @Test
    public void testGenerateTask() throws SQLException {
        Task task = new Task(1,"testGenerateTask", Priority.A, GenerationType.EVERY_DAY,
                LocalDate.now().minusDays(1), LocalDate.of(9999, 12, 31), LocalTime.of(23, 15, 56));
        controller.addTask(task);
        task.setId(getMaxIdFromTaskTable());
        resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id_task = " + task.getId());
        Assert.assertFalse(resultSet.next());
        controller.generateAutoTasks();
        resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id_task = " + task.getId());
        Assert.assertTrue(resultSet.next());
        controller.deleteTask(task);
    }
}
