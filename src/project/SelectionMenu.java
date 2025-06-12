package project;

import javax.swing.*;
import java.awt.*;

public class SelectionMenu extends JFrame {
    private final BinaryTree inventoryTree = InventoryStore.inventoryTree;

    public SelectionMenu() {
        super("SCZ Tech.inc – Main Menu");
        setSize(1280, 720);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                InventoryStore.save();
                dispose();
                System.exit(0);
            }
        });

        // Background image placeholder
        ImageIcon bgImg = new ImageIcon("background1.png");
        JLabel bgLabel = new JLabel(bgImg);
        bgLabel.setLayout(new BorderLayout());
        setContentPane(bgLabel);

        bgLabel.add(Header.build(), BorderLayout.NORTH);

        JButton addBtn = createButton("Add Stock", new Color(46, 204, 64), "add_icon.png");
        JButton sellBtn = createButton("Sell Stock", new Color(231, 76, 60), "sell_icon.png");

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 200));
        btnPanel.add(addBtn);
        btnPanel.add(sellBtn);
        bgLabel.add(btnPanel, BorderLayout.CENTER);

        addBtn.addActionListener(e -> new AddStockFrame(inventoryTree, this));
        sellBtn.addActionListener(e -> new SellFrame(inventoryTree));

        setVisible(true);
    }

    private JButton createButton(String text, Color bg, String iconPath) {
        JButton btn = new JButton(text, new ImageIcon(iconPath));
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setPreferredSize(new Dimension(150, 120));
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        return btn;
    }
}
