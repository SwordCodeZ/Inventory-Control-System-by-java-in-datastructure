package project;

import javax.swing.*;
import java.awt.*;

/** Simple banner with logo on left and two text lines on right. */
public class Header {

    public static JPanel build() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);

        // Logo on the left
        JLabel logo = new JLabel(new ImageIcon("logo.png"));
        logo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bar.add(logo, BorderLayout.WEST);

        // Two text lines on the right
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);

        JLabel line1 = new JLabel("SCZ Tech.inc");
        line1.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel line2 = new JLabel("We bring innovation and performance");
        line2.setFont(new Font("Arial", Font.ITALIC, 14));

        textPanel.add(line1);
        textPanel.add(line2);

        bar.add(textPanel, BorderLayout.CENTER);
        return bar;
    }
}
