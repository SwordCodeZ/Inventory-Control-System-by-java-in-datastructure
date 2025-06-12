package project;

import javax.swing.*;
import java.awt.*;

/** Displays multi-item bill with grand total. */
public class BillFrame extends JFrame {

    public BillFrame(String details, double grandTotal) {
        super("Sales Bill");

        setSize(350, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(Header.build(), BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.append("Items Sold\n----------------------------\n");
        area.append(details);
        area.append("----------------------------\n");
        area.append("Grand Total: $" + grandTotal + "\n");

        add(new JScrollPane(area), BorderLayout.CENTER);
    }
}
