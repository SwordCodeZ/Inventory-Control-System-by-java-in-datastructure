package project;

import javax.swing.*;
import java.awt.*;

public class LoadingScreen extends JWindow {

    public LoadingScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(10, 96, 245)); // light blue

        JLabel top = new JLabel("SCZ Tech.inc", SwingConstants.CENTER);
        top.setFont(new Font("Arial", Font.BOLD, 20));
        top.setForeground(new Color(225,255,255));
        panel.add(top, BorderLayout.NORTH);

        JLabel center = new JLabel("Loading....", SwingConstants.CENTER);
        center.setFont(new Font("Arial", Font.BOLD, 24));
        center.setForeground(new Color(0,0,0));
        panel.add(center, BorderLayout.CENTER);

        add(panel);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setVisible(true);

        // Fix: Create a single-use timer to open SelectionMenu once
        Timer timer = new Timer(2000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose(); // close splash
                new SelectionMenu(); // open menu once
            }
        });
        timer.setRepeats(false); // important!
        timer.start();
    }
}
