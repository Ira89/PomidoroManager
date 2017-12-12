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

import static org.junit.Assert.fail;

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
        Task task = new Task(2,"testAddTask", Priority.A, GenerationType.ONCE,
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
        Task task = new Task(3,"testAddTask", Priority.A, GenerationType.EVERY_DAY,
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
        Task task = new Task(4,"testDeleteTask", Priority.A, GenerationType.ONCE,
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
        Task task = new Task(5,"testDeleteAutoTask", Priority.A, GenerationType.EVERY_DAY,
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
        Task task = new Task(6,"testCloseTask", Priority.A, GenerationType.ONCE,
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
        Task task = new Task(7,"testWorkTime", Priority.A, GenerationType.ONCE,
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
        Task task = new Task(8,"testGenerateTask", Priority.A, GenerationType.EVERY_DAY,
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

    @Test
    public void testGenerateMultiTasks() throws SQLException {
        Task task1 = new Task(9,"testGenerateMultiTask1", Priority.A, GenerationType.EVERY_DAY,
                LocalDate.now().minusDays(1), LocalDate.of(9999, 12, 31), LocalTime.of(23, 15, 56));
        controller.addTask(task1);
        task1.setId(getMaxIdFromTaskTable());

        Task task2 = new Task(10,"testGenerateMultiTask2", Priority.A, GenerationType.EVERY_DAY,
                LocalDate.now().minusDays(1), LocalDate.of(9999, 12, 31), LocalTime.of(23, 15, 56));
        controller.addTask(task2);
        task2.setId(getMaxIdFromTaskTable());

        resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id_task = " + task1.getId());
        Assert.assertFalse(resultSet.next());
        resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id_task = " + task2.getId());
        Assert.assertFalse(resultSet.next());

        controller.generateAutoTasks();
        resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id_task = " + task1.getId());
        Assert.assertTrue(resultSet.next());
        resultSet = dbManager.executeQuery("SELECT * FROM active_task WHERE id_task = " + task2.getId());
        Assert.assertTrue(resultSet.next());
        controller.deleteTask(task1);
        controller.deleteTask(task2);
    }

    @Test
    public void testPlusWeek() throws SQLException {
        Task task = new Task(11, "plusWeek", Priority.A, GenerationType.EVERY_WEEK,
                LocalDate.of(2017, 12, 4), LocalDate.of(9999, 12, 31), LocalTime.of(23, 15, 56));
        controller.addTask(task);
        task.setId(getMaxIdFromTaskTable());

        controller.generateAutoTasks();
        resultSet = dbManager.executeQuery("SELECT * FROM task WHERE id = " + task.getId());
        try {
            resultSet.next();
            task = controller.parseTask(resultSet);
        } catch (Exception exc) {
            fail("invalid type");
        } finally {
            controller.deleteTask(task);
        }
        Assert.assertTrue(task.getStartDay().toString().equals(LocalDate.of(2017, 12, 11).toString()));
    }
}
