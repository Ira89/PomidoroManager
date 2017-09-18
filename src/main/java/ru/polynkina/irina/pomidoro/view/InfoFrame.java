package ru.polynkina.irina.pomidoro.view;

import javax.swing.*;
import java.awt.*;

public class InfoFrame extends JDialog {

    public InfoFrame(JFrame owner, String name, String text) {
        super(owner, name, true);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(size.width / 4, size.height / 4);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelLabel = new JPanel();
        JLabel label = new JLabel();
        label.setText("<html><br>" + text + "<br></html>");
        label.setForeground(Color.BLUE);
        panelLabel.add(label, BorderLayout.NORTH);

        JPanel panelButton = new JPanel();
        JButton ok = new JButton("OK");
        ok.addActionListener(e -> dispose());
        panelButton.add(ok, BorderLayout.PAGE_END);

        add(panelLabel, BorderLayout.NORTH);
        add(panelButton, BorderLayout.SOUTH);
    }
}
