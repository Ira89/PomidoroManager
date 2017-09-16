package ru.polynkina.irina.pomidoro;

import ru.polynkina.irina.pomidoro.controller.Controller;
import ru.polynkina.irina.pomidoro.db.DBManager;
import ru.polynkina.irina.pomidoro.view.PomidoroFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws Exception {

        DBManager dbManager = new DBManager("dbtest");
        Controller controller = new Controller(dbManager);
        controller.generateAutoTasks();
        SwingUtilities.invokeLater(() -> {
            PomidoroFrame frame = new PomidoroFrame(controller);
            frame.addWindowListener(new WindowListener() {
                public void windowClosing(WindowEvent e) {
                    Rectangle frameBounds = frame.getBounds();
                    Properties properties = new Properties();
                    properties.setProperty("xPos", String.valueOf(frameBounds.x));
                    properties.setProperty("yPos", String.valueOf(frameBounds.y));
                    properties.setProperty("width", String.valueOf(frameBounds.width));
                    properties.setProperty("height", String.valueOf(frameBounds.height));

                    try {
                        FileOutputStream output = new FileOutputStream(
                                System.getProperty("user.home") + File.separator + "pomidoro.properties");
                        properties.store(output, "Saved settings");
                        output.close();
                    } catch(Exception exc) {
                        exc.printStackTrace();
                    }
                    System.exit(0);
                }
                public void windowClosed(WindowEvent e) {}
                public void windowIconified(WindowEvent e) {}
                public void windowDeiconified(WindowEvent e) {}
                public void windowActivated(WindowEvent e) {}
                public void windowDeactivated(WindowEvent e) {}
                public void windowOpened(WindowEvent e) {}
            });
        });
    }
}
