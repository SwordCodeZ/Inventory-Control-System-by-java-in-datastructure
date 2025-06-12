package project;

import javax.swing.*;
import java.awt.*;

/** Modal dialog that checks hard-coded admin credentials. */
public class LoginDialog extends JDialog {

    private boolean success = false;

    public LoginDialog(JFrame parent) {
        super(parent, "Admin Login", true);

        setSize(350, 220);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(3, 1, 10, 10));

        // Row 1 – username
        JPanel userRow = new JPanel(new BorderLayout());
        userRow.add(new JLabel(new ImageIcon("user_icon.png")), BorderLayout.WEST);
        JTextField userField = new JTextField();
        userField.setBorder(BorderFactory.createTitledBorder("Username"));
        userRow.add(userField);
        add(userRow);

        // Row 2 – password
        JPanel passRow = new JPanel(new BorderLayout());
        passRow.add(new JLabel(new ImageIcon("pass_icon.png")), BorderLayout.WEST);
        JPasswordField passField = new JPasswordField();
        passField.setBorder(BorderFactory.createTitledBorder("Password"));
        passRow.add(passField);
        add(passRow);

        // Row 3 – login button
        JButton loginButton = new JButton("Login");
        add(loginButton);

        loginButton.addActionListener(e -> {
            String u = userField.getText().trim();
            String p = new String(passField.getPassword());
            if ("Taha.csz.inc".equals(u) && "/*-789+".equals(p)) {
                success = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Worng Password");
            }
        });
    }

    public boolean isOk() { return success; }
}
