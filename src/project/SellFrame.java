package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class SellFrame extends JFrame {

    private BinaryTree tree;
    private DefaultTableModel billModel;
    private static final String HISTORY = "inventory_history.txt";

    public SellFrame(BinaryTree tree) {
        super("Sell Inventory");
        this.tree = tree;

        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(Header.build(), BorderLayout.NORTH);

        billModel = new DefaultTableModel(new String[]{"Item No", "Name", "Price", "Qty", "Line Total"}, 0);
        JTable billTable = new JTable(billModel);
        add(new JScrollPane(billTable));

        JButton addBtn = new JButton("Add Item");
        JButton finishBtn = new JButton("Finish Sale");
        JButton backBtn = new JButton("Back");
        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn); btnPanel.add(finishBtn); btnPanel.add(backBtn);
        add(btnPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addItemToBill());
        finishBtn.addActionListener(e -> showBill());
        backBtn.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void addItemToBill() {
        String prefix = JOptionPane.showInputDialog(this, "Enter item name:");
        if (prefix == null || prefix.trim().isEmpty()) return;

        List<Item> matches = new ArrayList<>();
        for (Item i : tree.toList()) {
            if (i.name.toLowerCase().startsWith(prefix.toLowerCase())) {
                matches.add(i);
            }
        }

        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items found.");
            return;
        }

        String[] options = new String[matches.size()];
        for (int i = 0; i < matches.size(); i++) {
            options[i] = matches.get(i).name;
        }

        String chosen = (String) JOptionPane.showInputDialog(this, "Select item",
                "Matches", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (chosen == null) return;

        Item selected = null;
        for (Item i : matches) {
            if (i.name.equals(chosen)) {
                selected = i;
                break;
            }
        }

        if (selected == null) return;

        String qtyStr = JOptionPane.showInputDialog(this,
                "Quantity in stock: " + selected.quantity + "\nEnter quantity to sell:");
        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0 || qty > selected.quantity) {
                JOptionPane.showMessageDialog(this, "Invalid quantity.");
                return;
            }

            selected.quantity -= qty;
            InventoryStore.save(); // ✅ Save to file
            double total = qty * selected.price;

            billModel.addRow(new Object[]{
                    selected.itemNo, selected.name, selected.price, qty, total
            });

            logHistory("SELL", new Item(selected.itemNo, selected.name, selected.price, qty));

            //if (selected.quantity < 200) warnLowStock(selected);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid number.");
        }
    }

    /*private void warnLowStock(Item item) {
        int option = JOptionPane.showConfirmDialog(this,
                "Stock of " + item.name + " is below 200.\nContact supplier?",
                "Low Stock", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Order placed! Inventory will arrive in 2–3 weeks.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please contact supplier soon.");
        }
    }*/

    private void showBill() {
        if (billModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No items in bill.");
            return;
        }

        StringBuilder details = new StringBuilder();
        double grandTotal = 0;

        for (int r = 0; r < billModel.getRowCount(); r++) {
            String name = (String) billModel.getValueAt(r, 1);
            double price = (double) billModel.getValueAt(r, 2);
            int qty = (int) billModel.getValueAt(r, 3);
            double total = (double) billModel.getValueAt(r, 4);

            details.append(name).append(" x").append(qty)
                   .append(" @ ").append(price)
                   .append(" = ").append(total).append("\n");
            grandTotal += total;
        }

        new BillFrame(details.toString(), grandTotal).setVisible(true);
        dispose();
    }

    private void logHistory(String action, Item it) {
        try {
            FileWriter fw = new FileWriter(HISTORY, true);
            fw.write(LocalDateTime.now() + " | " + action + " | " +
                    it.itemNo + " | " + it.name +
                    " | price=" + it.price + " | qty=" + it.quantity + "\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
