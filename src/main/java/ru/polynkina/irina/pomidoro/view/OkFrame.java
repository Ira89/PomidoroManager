package ru.polynkina.irina.pomidoro.view;

import javax.swing.*;
import java.awt.*;

public class OkFrame extends JDialog {

    public OkFrame(JFrame owner, String text) {
        super(owner, text, true);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(size.width / 4, size.height / 4);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        JLabel label = new JLabel(text);
        add(label);

        JButton ok = new JButton();
        ok.setText("OK");
        ok.addActionListener(e -> dispose());
        add(ok);
        pack();
    }
}
